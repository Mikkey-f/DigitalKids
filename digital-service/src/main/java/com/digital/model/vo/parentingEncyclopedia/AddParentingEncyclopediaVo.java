package com.digital.model.vo.parentingEncyclopedia;

import lombok.Data;

import java.util.Date;

@Data
public class AddParentingEncyclopediaVo {
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

    private Date createTime;

    private Date updateTime;
}
