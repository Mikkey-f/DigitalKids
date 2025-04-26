package com.digital.model.vo.like;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/20 20:18
 */
@Data
public class LikeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long entityLikeCount;
    /**
     * 1 为点赞
     * 0 为不点赞
     */
    private int likeStatus;
}
