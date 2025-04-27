package com.digital.utils;

import com.digital.constant.TopicConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import com.digital.model.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 19:52
 */
@Slf4j
@Component
public class SseUtil {
    private static final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    @Autowired
    private MessageService messageService;
    /**
     * 创建连接
     */
    public SseEmitter connect(Long userId) {
        if (sseEmitterMap.containsKey(userId)) {
            //这种写法不能保证每个用户在某个时间段内只有一个连接
            //return sseEmitterMap.get(userId);
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            sseEmitterMap.remove(userId);
            sseEmitter.complete();
        }

        try {
            UUID uuid = UUID.randomUUID();
            String str = uuid.toString();
            String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);

            // 设置超时时间默认30s
            SseEmitter sseEmitter = new SseEmitter(30 * 1000L);
            // 告诉客户端初始连接建立标志
            sseEmitter.send(SseEmitter.event().id(temp).data(""));
            // 注册回调函数
            sseEmitter.onCompletion(completionCallBack(userId));
            sseEmitter.onTimeout(timeoutCallback(userId));
            sseEmitterMap.put(userId, sseEmitter);
            log.info("创建sse连接完成，当前用户为:{}",userId);
            return sseEmitter;
        } catch (IOException e) {
            log.info("创建sse连接异常，当前用户{}", userId);
        }
        return null;
    }

    public boolean sendMessage(Long toUserId, Long messageId, String message) {

        if (sseEmitterMap.containsKey(toUserId)) {
            SseEmitter sseEmitter = sseEmitterMap.get(toUserId);
            try {
                sseEmitter.send(SseEmitter.event().id(String.valueOf(messageId)).data(message));
                log.info("用户{},消息id:{},推送成功:{}", toUserId, messageId, message);
                return true;
            } catch (Exception e) {
                sseEmitterMap.remove(toUserId);
                log.info("用户{},消息id:{},推送异常:{}", toUserId, messageId, e.getMessage());
                sseEmitter.complete();
                return false;
            }
        } else {
            log.info("用户{}未上线", toUserId);
        }
        return false;
    }

    /**
     * 删除连接
     * @param userId
     */
    public void deleteUser(Long userId){
        removeUser(userId);
    }


    private static Runnable completionCallBack(Long userId) {
        return () -> {
            log.info("结束sse用户连接:{}", userId);
            removeUser(userId);
        };
    }

    private static Runnable timeoutCallback(Long userId) {
        return () -> {
            log.info("连接sse用户超时:{}", userId);
            removeUser(userId);
        };
    }


    public static void removeUser(Long userId) {
        if (sseEmitterMap.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            sseEmitterMap.remove(userId);
            sseEmitter.complete();
        } else {
            log.info("用户{}连接已经关闭", userId);
        }
    }

    public Map<Long, SseEmitter> listSseConnect() {
        return sseEmitterMap;
    }

}
