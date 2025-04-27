package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.EntityTypeConstant;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.mapper.ParentingEncyclopediaMapper;
import com.digital.model.entity.Comment;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.User;
import com.digital.model.request.page.PageReq;
import com.digital.model.request.parentingEncyclopedia.AddParentingEncyclopediaReq;
import com.digital.model.request.parentingEncyclopedia.UpdateParentingEncyclopediaReq;
import com.digital.model.vo.comment.CommentSonSonVo;
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
import org.springframework.data.domain.PageRequest;
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
    private ParentingEncyclopediaService parentingEncyclopediaService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ParentingEncyclopediaMapper parentingEncyclopediaMapper;

    /**
     * 得到相应stage的所有育儿百科
     * @param stage 0-8各个阶段， -1为全部
     * @return
     */
    @GetMapping("/stage/{stage}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Page<ParentingEncyclopedia>> getParentingEncyclopedia(@PathVariable Integer stage, PageReq pageReq) {
        long current = pageReq.getCurrent();
        long size = pageReq.getPageSize();
        Page<ParentingEncyclopedia> page = new Page<>(current, size);
        Page<ParentingEncyclopedia> parentingEncyclopediaPage = parentingEncyclopediaMapper.selectPage(page, stage == -1 ? null : new QueryWrapper<ParentingEncyclopedia>().eq("stage", stage));
        return Result.success(parentingEncyclopediaPage);
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
        long entityLikeCount = likeService.findEntityLikeCount(EntityTypeConstant.PARENTING_ENCYCLOPEDIA, id);
        getParentingEncyclopediaDetailVo.setLikeCount(entityLikeCount);
        int likeStatus = likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.PARENTING_ENCYCLOPEDIA, id);
        getParentingEncyclopediaDetailVo.setLikeStatus(likeStatus);
        // 针对实体，被筛选出来的评论列表
        List<Comment> filteredList = list.stream()
                .filter(comment -> comment.getTargetId() == 0)
                .toList();
        for (Comment comment : filteredList) {
            CommentVo commentVo = CommentVo.builder()
                    .id(comment.getId())
                    .getUserVo(userService.getUserVo(userService.getById(comment.getUserId())))
                    .content(comment.getContent())
                    .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment.getId()))
                    .likeStatus(likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.COMMENT, comment.getId()))
                    .build();
            List<CommentSonVo> listCommentSonVo = list.stream()
                    .filter(comment1 -> comment1.getTargetId() == comment.getId())
                    .map(comment1 -> CommentSonVo.builder()
                            .id(comment1.getId())
                            .content(comment1.getContent())
                            .getUserVo(userService.getUserVo(userService.getById(comment1.getUserId())))
                            .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment.getId()))
                            .likeStatus(likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.COMMENT, comment.getId()))
                            .build())
                    .toList();
            //二阶评论
            for (CommentSonVo commentSonVo : listCommentSonVo) {
                List<CommentSonSonVo> list1 = list.stream()
                        .filter(comment1 -> comment1.getTargetId() == commentSonVo.getId())
                        .map(comment1 -> CommentSonSonVo.builder()
                                .id(comment1.getId())
                                .content(comment1.getContent())
                                .getUserVo(userService.getUserVo(userService.getById(comment1.getUserId())))
                                .likeCount(likeService.findEntityLikeCount(EntityTypeConstant.COMMENT, comment.getId()))
                                .likeStatus(likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), EntityTypeConstant.COMMENT, comment.getId()))
                                .build())
                        .toList();
                commentSonVo.setListCommentSonSonVo(list1);
            }
            commentVo.setCommentSonVoList(listCommentSonVo);
            commentVos.add(commentVo);
        }

        BeanUtils.copyProperties(parentingEncyclopedia, getParentingEncyclopediaDetailVo);
        getParentingEncyclopediaDetailVo.setCommentVos(commentVos);
        return Result.success(getParentingEncyclopediaDetailVo);
    }
}
