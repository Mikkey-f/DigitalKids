package com.digital.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  19:29
 */
@Getter
@Setter
public class Event {

    private String topic;
    /**
     * 发送放id和接收方userId
     */
    private int fromUserId;
    private int toUserId;
    /**
     * 来自哪一个实体，实体类型和实体id标注出来
     */
    private int entityType;
    private int entityId;
    private Map<String, Object> data = new ConcurrentHashMap<>();

    public Event setData(String key, Object value) {
        data.put(key, value);
        return this;
    }
}
