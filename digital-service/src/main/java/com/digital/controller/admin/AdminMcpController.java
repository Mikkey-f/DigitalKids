package com.digital.controller.admin;

import com.digital.constant.TopicConstant;
import com.digital.event.EventProducer;
import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 15:20
 */
@RestController
@Slf4j
@RequestMapping("/admin/mcp")
public class AdminMcpController {
    @Autowired
    private EventProducer eventProducer;

    private static final String ToGoUtl = "/status/{requestId}";

    /**
     * 语音控制智能体
     * @param input 语音转文字后的输出
     * @return
     */
    @GetMapping("/dataBaseOperation")
    public Result<String> dataBaseOperation(String input) {
        String requestId = UUID.randomUUID().toString();
        String message = requestId + ":" + input;
        eventProducer.fireEventByQuestion(TopicConstant.TOPIC_MCP, message);
        return Result.success("请求已接收，正在处理，请轮询查询:" + ToGoUtl + "请求 ID：" + requestId);
    }
}
