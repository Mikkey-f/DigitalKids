package com.digital.event;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.digital.constant.StatusConstant;
import com.digital.constant.TopicConstant;
import com.digital.controller.StatusController;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.CommonEvent;
import com.digital.model.entity.Message;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.request.mcp.ChatRequest;
import com.digital.model.request.question.QuestionReq;
import com.digital.service.MessageService;
import com.digital.service.ParentingEncyclopediaService;
import com.digital.service.impl.ElasticsearchService;
import com.digital.utils.SseUtil;
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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.digital.constant.UserConstant.SYSTEM_USER_ID;

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

    private final WebClient webClientQuestion;
    private final WebClient webClientMcp;
    @Autowired
    private MessageService messageService;

    @Autowired
    private SseUtil sseUtil;

    private static final String MCP_IP = "127.0.0.1";
    private static final String MCP_PORT = "7777";


    public EventConsumer(WebClient.Builder webClientBuilder) {
        this.webClientQuestion = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.webClientMcp = webClientBuilder.baseUrl("http://" + MCP_IP + ":" + MCP_PORT).build();
    }

    /**
     * 评论-1 点赞-2
     * @param record
     */
    @KafkaListener(topics = {TopicConstant.TOPIC_COMMENT, TopicConstant.TOPIC_LIKE}, groupId = "digitalKids-consumer-group")
    public void handleCommentMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        CommonEvent commonEvent = JSONObject.parseObject(record.value(), CommonEvent.class);
        if (commonEvent == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }
        Message message = Message.builder()
                .fromId(SYSTEM_USER_ID)
                .isRead(0)
                .build();
        Object targetUserId = commonEvent.getData().get("targetUserId");
        message.setToId((Long) targetUserId);
        if (Objects.equals(commonEvent.getTopic(), TopicConstant.TOPIC_COMMENT)) {
            message.setTopicId(TopicConstant.TOPIC_COMMENT_ID);
        } else if (Objects.equals(commonEvent.getTopic(), TopicConstant.TOPIC_LIKE)) {
            message.setTopicId(TopicConstant.TOPIC_LIKE_ID);
        }

        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("entityType", String.valueOf(commonEvent.getEntityType()));
        map.put("entityId", String.valueOf(commonEvent.getEntityId()));
        map.put("SendUserId", String.valueOf(commonEvent.getFromUserId()));
        map.put("topicId", commonEvent.getTopic());

        String jsonString = JSONUtils.toJSONString(map);
        message.setContent(jsonString);

        // 系统发送提示消息
        sseUtil.sendMessage((Long)targetUserId, String.valueOf(message.getId()), message.getContent());

        boolean save = messageService.save(message);
        if (!save) {
            throw new BusinessException(ResultErrorEnum.OPERATION_ERROR);
        }
    }

    @KafkaListener(topics = {TopicConstant.TOPIC_PUBLISH_PARENTING_ENCY}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaPublishMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        CommonEvent commonEvent = JSONObject.parseObject(record.value(), CommonEvent.class);
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
        CommonEvent commonEvent = JSONObject.parseObject(record.value(), CommonEvent.class);
        if (commonEvent == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }
//        parentingEncyclopediaService.removeById(commonEvent.getEntityId());
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

        Mono<ResponseEntity<String>> responseEntityMono = webClientQuestion.method(HttpMethod.POST)
                .uri("/run_workflow")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSON.toJSONString(questionReq))
                .retrieve()
                .toEntity(String.class);

        try {
            ResponseEntity<String> response = responseEntityMono.block();
            if (response != null) {
                StatusController.saveSession(requestId, response.getBody());
            }
        } catch (RuntimeException e) {
            WebClientResponseException.UnprocessableEntity unprocessableEntity = (WebClientResponseException.UnprocessableEntity) e;
            log.error("422 Unprocessable Entity. Status code: {}", unprocessableEntity.getStatusCode());
            log.error("Error details: {}", unprocessableEntity.getResponseBodyAsString());
            throw new BusinessException(ResultErrorEnum.SMART_AGENT_ERROR);
        }
    }

    @KafkaListener(topics = {TopicConstant.TOPIC_MCP}, groupId = "digitalKids-consumer-group")
    public void handleMcpMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        String message = record.value();
        String[] split = message.split(":");
        String requestId = split[0];
        String question = split[1];
        ChatRequest chatRequest = new ChatRequest(question);

        Mono<ResponseEntity<String>> responseEntityMono = webClientMcp.method(HttpMethod.POST)
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSON.toJSONString(chatRequest))
                .retrieve()
                .toEntity(String.class);

        try {
            ResponseEntity<String> response = responseEntityMono.block();
            if (response != null) {
                StatusController.saveSession(requestId, response.getBody());
            }
        } catch (RuntimeException e) {
            WebClientResponseException.UnprocessableEntity unprocessableEntity = (WebClientResponseException.UnprocessableEntity) e;
            log.error("422 Unprocessable Entity. Status code: {}", unprocessableEntity.getStatusCode());
            log.error("Error details: {}", unprocessableEntity.getResponseBodyAsString());
            throw new BusinessException(ResultErrorEnum.SMART_AGENT_ERROR);
        }
    }
}
