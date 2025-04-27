package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.EntityTypeConstant;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Category;
import com.digital.model.entity.Comment;
import com.digital.model.entity.Product;
import com.digital.model.entity.User;
import com.digital.model.request.category.CategoryAddReq;
import com.digital.model.request.category.CategoryUpdateReq;
import com.digital.model.request.page.PageReq;
import com.digital.model.request.product.ProductAddReq;
import com.digital.model.request.product.ProductUpdateReq;
import com.digital.model.vo.category.GetListCategoryVo;
import com.digital.model.vo.comment.CommentSonVo;
import com.digital.model.vo.comment.CommentVo;
import com.digital.model.vo.product.GetProductVo;
import com.digital.result.Result;
import com.digital.service.CategoryService;
import com.digital.service.CommentService;
import com.digital.service.ProductService;
import com.digital.service.UserService;
import com.digital.service.impl.LikeService;
import com.digital.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 15:11
 */
@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查看商品通过id
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetProductVo> getProductById(@PathVariable Integer id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        if (id == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        List<Comment> list = commentService.list(new QueryWrapper<Comment>().eq("entity_type", EntityTypeConstant.PRODUCT).eq("entity_id", id));
        List<CommentVo> commentVos = new ArrayList<>();
        GetProductVo getProductVo = new GetProductVo();

        //添加总点赞数量和状态
        long entityLikeCount = likeService.findEntityLikeCount(EntityTypeConstant.PRODUCT, id);
        getProductVo.setLikeCount(BigInteger.valueOf(entityLikeCount));
        int likeStatus = likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.PRODUCT, id);
        getProductVo.setLikeStatus(BigInteger.valueOf(likeStatus));
        // 针对实体，被筛选出来的评论列表
        List<Comment> filteredList = list.stream()
                .filter(comment -> comment.getTargetId() == 0)
                .toList();
        for (Comment comment : filteredList) {
            CommentVo commentVo = CommentVo.builder()
                    .getUserVo(userService.getUserVo(userService.getById(comment.getUserId())))
                    .content(comment.getContent())
                    .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment.getId()))
                    .likeStatus(likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.COMMENT, comment.getId()))
                    .build();
            List<CommentSonVo> listCommentSonVo = list.stream()
                    .filter(comment1 -> comment1.getTargetId() == comment.getId())
                    .map(comment1 -> CommentSonVo.builder()
                            .content(comment1.getContent())
                            .getUserVo(userService.getUserVo(userService.getById(comment1.getUserId())))
                            .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment1.getId()))
                            .likeStatus(likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.COMMENT, comment1.getId()))
                            .build())
                    .toList();
            commentVo.setCommentSonVoList(listCommentSonVo);
            commentVos.add(commentVo);
        }

        BeanUtils.copyProperties(product, getProductVo);
        getProductVo.setCommentVos(commentVos);
        return Result.success(getProductVo);
    }

    /**
     * 得到商品列表在某个类别下
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/product")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Page<GetProductVo>> getProductListByCategoryId(Integer pageNum, Integer pageSize, Integer categoryId) {
        if (pageNum == null || pageSize == null || categoryId == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }


        Page<Product> categoryPage = productService.page(new Page<>(pageNum, pageSize), new QueryWrapper<Product>().eq("category_id", categoryId));
        List<GetProductVo> getProductVoList = categoryPage.getRecords().stream()
                .map(product -> {
                    GetProductVo getProductVo = new GetProductVo();
                    BeanUtils.copyProperties(product, getProductVo);
                    return getProductVo;
                })
                .toList();
        Page<GetProductVo> getProductVoPage = new Page<>();
        getProductVoPage.setRecords(getProductVoList);
        return Result.success(getProductVoPage);
    }



}
