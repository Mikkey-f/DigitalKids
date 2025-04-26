package com.digital.controller;

import com.digital.constant.EntityTypeConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.event.EventProducer;
import com.digital.model.entity.Comment;
import com.digital.model.entity.CommonEvent;
import com.digital.model.entity.User;
import com.digital.model.request.comment.CommentAddReq;
import com.digital.result.Result;
import com.digital.service.CommentService;
import com.digital.service.ParentingEncyclopediaService;
import com.digital.service.ProductService;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.digital.constant.EntityTypeConstant.PARENTING_ENCYCLOPEDIA;
import static com.digital.constant.TopicConstant.TOPIC_COMMENT;
import static com.digital.constant.TopicConstant.TOPIC_PUBLISH_PARENTING_ENCY;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/14 19:11
 */
@RestController

@Slf4j
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    ProductService productService;

    @Autowired
    ParentingEncyclopediaService parentingEncyclopediaService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;
    /**
     * 添加评论
     * @return
     */
    @PostMapping(path = "/add}")
    public Result addComment(@RequestBody CommentAddReq commentAddReq,
                             HttpServletRequest request) {
        if (commentAddReq.getContent() == null || commentAddReq.getContent().trim().equals("")
        || commentAddReq.getEntityType() == null || commentAddReq.getEntityId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddReq, comment);
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.LOGIN_IS_FAILURE.getMessage());
        }
        comment.setUserId(Math.toIntExact(loginUser.getId()));
        comment.setStatus(0);

        CommonEvent commonEvent = CommonEvent.builder()
                .topic(TOPIC_COMMENT)
                .entityType(commentAddReq.getEntityType())
                .entityId(commentAddReq.getEntityId())
                .fromUserId(loginUser.getId())
                .build();
        if (commonEvent.getEntityType() == EntityTypeConstant.PARENTING_ENCYCLOPEDIA) {
            commonEvent.setData("targetUserId" ,parentingEncyclopediaService.getById(commentAddReq.getEntityId()).getUserId());
        }
        eventProducer.fireEvent(commonEvent);

        return Result.success();
    }
}
