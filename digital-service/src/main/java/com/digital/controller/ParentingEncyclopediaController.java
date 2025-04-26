package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.EntityTypeConstant;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Comment;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.User;
import com.digital.model.request.parentingEncyclopedia.AddParentingEncyclopediaReq;
import com.digital.model.request.parentingEncyclopedia.UpdateParentingEncyclopediaReq;
import com.digital.model.vo.comment.CommentSonVo;
import com.digital.model.vo.comment.CommentVo;
import com.digital.model.vo.parentingEncyclopedia.AddParentingEncyclopediaVo;
import com.digital.model.vo.parentingEncyclopedia.GetParentingEncyclopediaDetailVo;
import com.digital.model.vo.parentingEncyclopedia.GetParentingEncyclopediaVo;
import com.digital.model.vo.parentingEncyclopedia.UpdateParentingEncyclopediaVo;
import com.digital.model.vo.product.GetProductVo;
import com.digital.result.Result;
import com.digital.service.CommentService;
import com.digital.service.ParentingEncyclopediaService;
import com.digital.service.UserService;
import com.digital.service.impl.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/parentingEncyclopedia")
public class ParentingEncyclopediaController {

    @Autowired
    ParentingEncyclopediaService parentingEncyclopediaService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;

    /**
     * 得到相应stage的所有育儿百科
     * @param stage
     * @return
     */
    @GetMapping("/{stage}")
    @AuthCheck(mustRole = "guest")
    public Result<List<ParentingEncyclopedia>> getParentingEncyclopedia(@PathVariable Integer stage) {
        return parentingEncyclopediaService.get(stage);
    }

    /**
     * 删除对应id的育儿百科
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = "admin")
    public Result deleteParentingEncyclopedia(@PathVariable Integer id) {
        Result result = parentingEncyclopediaService.delete(id);
        return result;
    }

    /**
     * 修改育儿百科内容
     * @param updateParentingEncyclopediaReq
     * @return
     */
    @PutMapping
    @AuthCheck(mustRole = "admin")
    public Result<UpdateParentingEncyclopediaVo> updateParentingEncyclopediaVoResult(@RequestBody UpdateParentingEncyclopediaReq updateParentingEncyclopediaReq) {
        Result result = parentingEncyclopediaService.EncyclopediaUpdate(updateParentingEncyclopediaReq);
        return result;
    }

    /**
     * 添加育儿百科
     * @param addParentingEncyclopediaReq
     * @return
     */
    @PostMapping
    @AuthCheck(mustRole = "admin")
    public Result<AddParentingEncyclopediaVo> addParentingEncyclopediaVoResult(@RequestBody AddParentingEncyclopediaReq addParentingEncyclopediaReq) {
        Result result = parentingEncyclopediaService.Add(addParentingEncyclopediaReq);
        return result;
    }

    /**
     * 根据百科id查找百科具体信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetParentingEncyclopediaDetailVo> getParentingEncyclopediaDetailVoResult(@PathVariable Integer id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        if (id == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        ParentingEncyclopedia parentingEncyclopedia = parentingEncyclopediaService.getById(id);
        if (parentingEncyclopedia == null) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        List<Comment> list = commentService.list(new QueryWrapper<Comment>().eq("entity_type", EntityTypeConstant.PARENTING_ENCYCLOPEDIA).eq("entity_id", id));
        List<CommentVo> commentVos = new ArrayList<>();
        GetParentingEncyclopediaDetailVo getParentingEncyclopediaDetailVo = new GetParentingEncyclopediaDetailVo();

        //添加总点赞数量和状态
        long entityLikeCount = likeService.findEntityLikeCount(EntityTypeConstant.PRODUCT, id);
        getParentingEncyclopediaDetailVo.setLikeCount(entityLikeCount);
        int likeStatus = likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.PARENTING_ENCYCLOPEDIA, id);
        getParentingEncyclopediaDetailVo.setLikeStatus(likeStatus);
        // 针对实体，被筛选出来的评论列表
        List<Comment> filteredList = list.stream()
                .filter(comment -> comment.getTargetId() == 0)
                .toList();
        for (Comment comment : filteredList) {
            CommentVo commentVo = CommentVo.builder()
                    .getUserVo(userService.getUserVo(userService.getById(comment.getUserId())))
                    .content(comment.getContent())
                    .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment.getId()))
                    .likeStatus(likeService.findUserLikeCount(Math.toIntExact(loginUser.getId())))
                    .build();
            List<CommentSonVo> listCommentSonVo = list.stream()
                    .filter(comment1 -> comment1.getTargetId() == comment.getId())
                    .map(comment1 -> CommentSonVo.builder()
                            .content(comment1.getContent())
                            .getUserVo(userService.getUserVo(userService.getById(comment1.getUserId())))
                            .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment.getId()))
                            .likeStatus(likeService.findUserLikeCount(Math.toIntExact(loginUser.getId())))
                            .build())
                    .toList();
            commentVo.setCommentSonVoList(listCommentSonVo);
            commentVos.add(commentVo);
        }

        BeanUtils.copyProperties(parentingEncyclopedia, getParentingEncyclopediaDetailVo);
        getParentingEncyclopediaDetailVo.setCommentVos(commentVos);
        return Result.success(getParentingEncyclopediaDetailVo);
    }
}
