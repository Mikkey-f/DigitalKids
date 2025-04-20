package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("from_id")
    private Long fromId;
    @TableField("to_id")
    private Long toId;
    @TableField("topic_id")
    private Long topicId;

    private String content;
    @TableField("is_read")
    private Integer isRead;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
}