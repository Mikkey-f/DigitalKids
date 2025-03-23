package com.digital.controller;

import com.alibaba.fastjson.JSONObject;
import com.digital.annotation.AuthCheck;
import com.digital.constant.TopicConstant;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ResultSuccessEnum;
import com.digital.event.EventProducer;
import com.digital.model.entity.CommonEvent;
import com.digital.model.request.question.QuestionReq;
import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/23 14:20
 */
@Slf4j
@RestController
public class QuestionController {

    @Autowired
    private EventProducer eventProducer;

    private static final String ToGoUtl = "/status/{requestId}";

    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @PostMapping("/question")
    public Result question(@RequestBody QuestionReq questionReq) {
        String requestId = UUID.randomUUID().toString();
        String message = requestId + ":" + questionReq.getQuestion();
        eventProducer.fireEventByQuestion(TopicConstant.TOPIC_QUESTION, message);


        return Result.success("请求已接收，正在处理，请轮询查询:" + ToGoUtl + "请求 ID：" + requestId);
    }
}
