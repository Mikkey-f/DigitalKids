package com.digital.controller;

import com.digital.constant.EntityTypeConstant;
import com.digital.constant.TopicConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.event.EventProducer;
import com.digital.model.entity.CommonEvent;
import com.digital.model.entity.Message;
import com.digital.model.entity.User;
import com.digital.model.vo.like.LikeVo;
import com.digital.result.Result;
import com.digital.service.MessageService;
import com.digital.service.UserService;
import com.digital.service.impl.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.digital.constant.TopicConstant.TOPIC_LIKE;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/20 19:31
 */
@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    /**
     * 给唯一实体(商品，百科，评论)点赞
     * @param entityType 商品类别
     * @param entityId 商品id
     * @param entityUserId 0 为系统发布（商品和百科） 非0为唯一实体为评论
     * @param request
     * @return
     */
    @PostMapping(path = "/like")
    public Result<LikeVo> like(int entityType, int entityId, int entityUserId, HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return Result.error(ResultErrorEnum.NOT_LOGIN_USER.getMessage());
        }
        // 实现点赞
        likeService.like(Math.toIntExact(loginUser.getId()), entityType, entityId, entityUserId);
        // 数量
        long entityLikeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(Math.toIntExact(loginUser.getId()), entityType, entityId);
        Map<String, Object> map = new ConcurrentHashMap<>();

        // 触发点赞事件
        if (likeStatus == 1) {
            CommonEvent commonEvent = CommonEvent.builder()
                    .topic(TOPIC_LIKE)
                    .entityType(entityType)
                    .entityId(entityId)
                    .fromUserId(loginUser.getId())
                    .build();
            map.put("targetUserId", entityUserId);
            commonEvent.setData(map);
            eventProducer.fireEvent(commonEvent);
        }
        LikeVo likeVo = new LikeVo();
        likeVo.setLikeStatus(likeStatus);
        likeVo.setEntityLikeCount(entityLikeCount);
        return Result.success(likeVo);
    }
}
