package com.digital.model.vo.parentingEncyclopedia;

import com.digital.model.vo.comment.CommentVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 14:38
 */
@Data
public class GetParentingEncyclopediaDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 0:备孕期, 1:孕产期管理, 2:产褥期, 3:产后恢复, 4:0-1岁宝宝, 5:1-2岁宝宝, 6:2-3岁宝宝, 7:3-5岁宝宝, 8:5-10岁宝宝, 9:10-15岁宝宝
     */
    private Integer stage;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String content;

    private Long likeCount;

    private Integer likeStatus;

    private List<CommentVo> commentVos;

    private Date createTime;

    private Date updateTime;
}
