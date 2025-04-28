package com.digital.utils;

import com.alibaba.fastjson.JSON;
import com.digital.constant.UserConstant;
import com.digital.controller.StatusController;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ThreeJsEnum;
import com.digital.exception.BusinessException;
import com.digital.model.request.question.QuestionReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 21:18
 */
@Slf4j
@Component
public class ThreeJsUtil {

    private final WebClient webClientQuestion;
    private final static String FIX = "的推荐建议，限制30字。不要复述我说的话，只给出建议。";
    private final static String PREFIX = "给我";

    public ThreeJsUtil(WebClient.Builder webClientBuilder) {
        this.webClientQuestion = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    public String getQuestion(String json, Integer code) {
        return json + PREFIX + getBUWei(code) + FIX;
    }

    public String getRecommendation(String question) {

        QuestionReq questionReq = new QuestionReq(question);
        Mono<ResponseEntity<String>> responseEntityMono = webClientQuestion.method(HttpMethod.POST)
                .uri("/run_workflow")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSON.toJSONString(questionReq))
                .retrieve()
                .toEntity(String.class);

        try {
            ResponseEntity<String> response = responseEntityMono.block();
            return response.getBody();
        } catch (RuntimeException e) {
            WebClientResponseException.UnprocessableEntity unprocessableEntity = (WebClientResponseException.UnprocessableEntity) e;
            log.error("422 Unprocessable Entity. Status code: {}", unprocessableEntity.getStatusCode());
            log.error("Error details: {}", unprocessableEntity.getResponseBodyAsString());
            throw new BusinessException(ResultErrorEnum.SMART_AGENT_ERROR);
        }
    }

    private static String getBUWei(int code) {
        if (code == ThreeJsEnum.HAND.getCode()) {
            return ThreeJsEnum.HAND.getName();
        } else if (code == ThreeJsEnum.FOOT.getCode()) {
            return ThreeJsEnum.FOOT.getName();
        } else if (code == ThreeJsEnum.BODY.getCode()) {
            return ThreeJsEnum.BODY.getName();
        } else if (code == ThreeJsEnum.ARM.getCode()) {
            return ThreeJsEnum.ARM.getName();
        } else if (code == ThreeJsEnum.HEAD.getCode()) {
            return ThreeJsEnum.HEAD.getName();
        } else if (code == ThreeJsEnum.LEG.getCode()) {
            return ThreeJsEnum.LEG.getName();
        } else if (code == ThreeJsEnum.SHOULDER.getCode()) {
            return ThreeJsEnum.SHOULDER.getName();
        } else if (code == ThreeJsEnum.VISUAL.getCode()) {
            return ThreeJsEnum.VISUAL.getName();
        } else if (code == ThreeJsEnum.ORAL.getCode()) {
            return ThreeJsEnum.ORAL.getName();
        } else if (code == ThreeJsEnum.ENDOCRINE.getCode()) {
            return ThreeJsEnum.ENDOCRINE.getName();
        } else if (code == ThreeJsEnum.ENT.getCode()) {
            return ThreeJsEnum.ENT.getName();
        } else {
            return ThreeJsEnum.RESPIRATORY.getName();
        }
    }


}
