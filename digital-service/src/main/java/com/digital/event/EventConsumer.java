package com.digital.event;

import com.alibaba.fastjson.JSONObject;
import com.digital.constant.TopicConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.Event;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.service.ParentingEncyclopediaService;
import com.digital.service.impl.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

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


    @KafkaListener(topics = {TopicConstant.TOPIC_COMMENT}, groupId = "digitalKids-consumer-group")
    public void handleCommentMessage(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = {TopicConstant.TOPIC_PUBLISH_PARENTING_ENCY}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaPublishMessage(ConsumerRecord<String, String> record) {
        if (record == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            throw new BusinessException(ResultErrorEnum.MQ_ERROR);
        }

        ParentingEncyclopedia parentingEncyclopedia = parentingEncyclopediaService.getById(event.getEntityId());
        elasticsearchService.saveEncyclopedia(parentingEncyclopedia);
    }

    @KafkaListener(topics = {TopicConstant.TOPIC_DELETE}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaDeleteMessage(ConsumerRecord<String, String> record) {

    }
}
