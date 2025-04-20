package com.digital.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.digital.constant.StatusConstant;
import com.digital.constant.TopicConstant;
import com.digital.controller.StatusController;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.CommonEvent;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.request.question.QuestionReq;
import com.digital.service.ParentingEncyclopediaService;
import com.digital.service.impl.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.socket.WebSocketHandler;
import reactor.core.publisher.Mono;

import java.net.http.WebSocket;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  19:29
 */
@Component
@Slf4j
public class EventConsumer {

    @Autowired
    private ParentingEncyclopediaService parentingEncyclopediaService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    private final WebClient webClient;


    public EventConsumer(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }


    @KafkaListener(topics = {TopicConstant.TOPIC_COMMENT, TopicConstant.TOPIC_LIKE}, groupId = "digitalKids-consumer-group")
    public void handleCommentMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        CommonEvent commonEvent = JSONObject.parseObject(record.value().toString(), CommonEvent.class);
        if (commonEvent == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }
    }

    @KafkaListener(topics = {TopicConstant.TOPIC_PUBLISH_PARENTING_ENCY}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaPublishMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        CommonEvent commonEvent = JSONObject.parseObject(record.value().toString(), CommonEvent.class);
        if (commonEvent == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        ParentingEncyclopedia parentingEncyclopedia = parentingEncyclopediaService.getById(commonEvent.getEntityId());
        elasticsearchService.saveEncyclopedia(parentingEncyclopedia);
    }

    @KafkaListener(topics = {TopicConstant.TOPIC_DELETE_PARENTING_ENCY}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaDeleteMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }
        CommonEvent commonEvent = JSONObject.parseObject(record.value().toString(), CommonEvent.class);
        if (commonEvent == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }
        parentingEncyclopediaService.removeById(commonEvent.getEntityId());
        elasticsearchService.deleteEncyclopedia((int) commonEvent.getEntityId());
    }

    @KafkaListener(topics = {TopicConstant.TOPIC_QUESTION}, groupId = "digitalKids-consumer-group")
    public void handleQuestionMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        String message = record.value();
        String[] split = message.split(":");
        String requestId = split[0];
        String question = split[1];
        QuestionReq questionReq = new QuestionReq(question);

        Mono<ResponseEntity<String>> responseEntityMono = webClient.method(HttpMethod.POST)
                .uri("/run_workflow")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSON.toJSONString(questionReq))
                .retrieve()
                .toEntity(String.class);

        try {
            ResponseEntity<String> response = responseEntityMono.block();
            StatusController.saveSession(requestId, response.getBody());
        } catch (RuntimeException e) {
            WebClientResponseException.UnprocessableEntity unprocessableEntity = (WebClientResponseException.UnprocessableEntity) e;
            log.error("422 Unprocessable Entity. Status code: {}", unprocessableEntity.getStatusCode());
            log.error("Error details: {}", unprocessableEntity.getResponseBodyAsString());
            throw new BusinessException(ResultErrorEnum.SMART_AGENT_ERROR);
        }
    }
}
