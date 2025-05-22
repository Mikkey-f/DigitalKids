package com.digital.event;

import com.alibaba.fastjson.JSONObject;
import com.digital.model.entity.CommonEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  19:29
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 处理事件
    public void fireEvent(CommonEvent commonEvent) {
        // 将事件发送到指定主题
        kafkaTemplate.send(commonEvent.getTopic(), JSONObject.toJSONString(commonEvent));
    }

    // 处理事件
    public void fireEventByQuestion(String topic, String question) {
        // 将事件发送到指定主题
        kafkaTemplate.send(topic, question);
    }
    // 处理事件
    public void fireEventByImage(String topic, String filePath) {
        // 将事件发送到指定主题
        kafkaTemplate.send(topic, filePath);
    }

}
