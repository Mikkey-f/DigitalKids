package com.digital.model.vo.comment;

import com.digital.model.vo.user.GetUserVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 00:43
 */
@Data
@Builder
public class CommentSonVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private GetUserVo getUserVo;

    private Long likeCount;

    private Integer likeStatus;

    private String content;

    private List<CommentSonSonVo> listCommentSonSonVo;
}
