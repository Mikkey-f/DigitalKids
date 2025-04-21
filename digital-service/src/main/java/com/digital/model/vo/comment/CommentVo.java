package com.digital.model.vo.comment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.digital.model.vo.user.GetUserVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 00:35
 */
@Data
@Builder
public class CommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private GetUserVo getUserVo;

    private String content;

    private Long likeCount;

    private Integer likeStatus;

    private List<CommentSonVo> commentSonVoList;
}
