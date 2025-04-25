package com.digital.controller;

import com.digital.constant.TopicConstant;
import com.digital.event.EventProducer;
import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 21:41
 */
@RestController
@Slf4j
public class McpController {

    @Autowired
    private EventProducer eventProducer;

    private static final String ToGoUtl = "/status/{requestId}";


    @GetMapping("/dataBaseOperation")
    public Result dataBaseOperation(String input) {
        String requestId = UUID.randomUUID().toString();
        String message = requestId + ":" + input;
        eventProducer.fireEventByQuestion(TopicConstant.TOPIC_MCP, message);
        return Result.success("请求已接收，正在处理，请轮询查询:" + ToGoUtl + "请求 ID：" + requestId);
    }
}
