package com.digital.model.vo.comment;

import com.digital.model.vo.user.GetUserVo;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 10:35
 */
@Data
@Slf4j
@Builder
public class CommentSonSonVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private GetUserVo getUserVo;

    private Long likeCount;

    private Integer likeStatus;

    private String content;
}
