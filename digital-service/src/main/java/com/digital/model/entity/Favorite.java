package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  17:26
 */
@Data
@TableName("favorite")
public class Favorite {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("entity_id")
    private Integer entityId;

    @TableField("user_id")
    private Long userId;

    @TableField("entity_type")
    private Integer entityType;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}