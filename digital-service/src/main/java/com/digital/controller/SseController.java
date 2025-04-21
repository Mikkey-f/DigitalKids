package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.result.Result;
import com.digital.utils.SseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
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

    @GetMapping(value = "/createSseConnect", produces = "text/event-stream;charset=UTF-8")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<SseEmitter> createSseConnect(Long userId) {
        return Result.success(sseUtil.connect(userId));
    }

    @PostMapping("/sendMessage")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> sendMessage(Long userId, String messageId, String message) {
        boolean b = sseUtil.sendMessage(userId, messageId, message);
        return Result.success(b);
    }

    @GetMapping("/listSseConnect")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Map<Long, SseEmitter>> listSseConnect() {
        Map<Long, SseEmitter> sseEmitterMap = sseUtil.listSseConnect();
        return Result.success(sseEmitterMap);
    }

    @GetMapping("/closeSseConnect")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> closeSseConnect(Long userId) {
        sseUtil.deleteUser(userId);
        return Result.success(true);
    }
}
