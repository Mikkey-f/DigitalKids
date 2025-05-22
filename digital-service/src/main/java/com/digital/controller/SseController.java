package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.TopicConstant;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Message;
import com.digital.result.Result;
import com.digital.service.MessageService;
import com.digital.service.UserService;
import com.digital.utils.SseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 20:15
 */
@RestController
@Slf4j
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseUtil sseUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    /**
     * 创建sse半双工连接
     * @param request 请求
     * @return 返回SseEmitter
     */
    @GetMapping(value = "/createSseConnect", produces = "text/event-stream;charset=UTF-8")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public SseEmitter createSseConnect(HttpServletRequest request) {
        return sseUtil.connect(userService.getLoginUser(request).getId());
    }

    /**
     * 用户向另一个用户实现局域内交流
     * @param message 消息内容
     * @return 是否成功
     */
    @PostMapping("/sendMessage")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> sendMessage(Long toUserId, String message, HttpServletRequest request) {
        Message build = Message.builder()
                .toId(toUserId)
                .topicId(TopicConstant.TOPIC_TALK_ID)
                .fromId(userService.getLoginUser(request).getId())
                .content(message)
                .isRead(0)
                .build();
        boolean save = messageService.save(build);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        sseUtil.sendMessage(toUserId, build.getId(), message);
        return Result.success(true);
    }


    /**
     * 用户关闭自己的连接
     * @param request 请求
     * @return 是否成功
     */
    @GetMapping("/closeSseConnect")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> closeSseConnect(HttpServletRequest request) {
        sseUtil.deleteUser(userService.getLoginUser(request).getId());
        return Result.success(true);
    }
}
