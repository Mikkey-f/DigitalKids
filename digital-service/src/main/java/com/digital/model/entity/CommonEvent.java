package com.digital.model.entity;

import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  19:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonEvent {

    private String topic;
    /**
     * 发送方id和接收方userId
     */
    private long fromUserId;
    /**
     * 实体类型：百科，商品
     */
    private int entityType;
    /**
     * 实体id
     */
    private long entityId;
    private Map<String, Object> data;

}
