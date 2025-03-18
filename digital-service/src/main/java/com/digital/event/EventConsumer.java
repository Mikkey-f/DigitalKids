package com.digital.event;

import com.digital.constant.TopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.internals.Topic;
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


    @KafkaListener(topics = {TopicConstant.TOPIC_COMMENT}, groupId = "digitalKids-consumer-group")
    public void handleCommentMessage(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = {TopicConstant.TOPIC_PUBLISH}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaPublishMessage(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = {TopicConstant.TOPIC_DELETE}, groupId = "digitalKids-consumer-group")
    public void handleEncyclopediaDeleteMessage(ConsumerRecord<String, String> record) {

    }
}
