package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/14 19:12
 */
@Data
@TableName("comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    /**
     * 发评论用户的id
     */
    @TableField("user_id")
    private int userId;
    /**
     * 目标类型,1 百科 2 商品
     */
    @TableField("entity_type")
    private int entityType;
    /**
     * 目标实体（百科，商品）id
     */
    @TableField("entity_id")
    private int entityId;
    /**
     * 0时是该实体，其他id是评论
     */
    @TableField("target_id")
    private int targetId;
    /**
     * 内容
     */
    private String content;
    /**
     * 评论状态，未读0 已读1 删除2
     */
    private int status;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
}
