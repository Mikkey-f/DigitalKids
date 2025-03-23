package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.RoleEnum;
import com.digital.model.request.question.QuestionReq;
import com.digital.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.client.reactive.WebClientProvider;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/23 14:20
 */
@Slf4j
@RestController
public class QuestionController {

    private WebClient webClient;

    @Value("${RedirectUrl}")
    private String redirectUrl;

    public QuestionController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @PostMapping("/question")
    public Result<String> question(@RequestBody QuestionReq questionReq) {
//        System.out.println(redirectUrl);
        Mono<ResponseEntity<String>> responseEntityMono = webClient.method(HttpMethod.POST)
                .uri("/run_workflow")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(questionReq)
                .retrieve()
                .toEntity(String.class);


        return Result.success(responseEntityMono.block().getBody());
    }
}
