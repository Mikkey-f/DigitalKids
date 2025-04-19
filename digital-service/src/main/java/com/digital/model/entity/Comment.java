package com.digital.model.entity;

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
    private int id;
    /**
     * 发评论用户的id
     */
    private int userId;
    /**
     * 目标类型,1 百科 2 商品
     */
    private int entityType;
    /**
     * 目标实体（百科，商品）id
     */
    private int entityId;
    /**
     * 0时是该实体，其他id是评论
     */
    private int targetId;
    /**
     * 内容
     */
    private String content;
    /**
     * 评论状态，未读0 已读1 删除2
     */
    private int status;
    private Date createTime;
    private Date updateTime;
}
