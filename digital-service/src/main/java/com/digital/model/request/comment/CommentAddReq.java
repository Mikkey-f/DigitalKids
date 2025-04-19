package com.digital.model.request.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/14 19:28
 */
@Data
public class CommentAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer entityType;

    private Integer entityId;

    private String content;
}
