package com.digital.model.request.search;

import com.digital.model.request.page.PageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-21  20:03
 */
@Data
public class SearchParEncyByStageReq implements Serializable {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 时期
     */
    private Integer stage;
    /**
     * 页数
     */
    private PageReq pageReq;

    private static final long serialVersionUID = 1L;
}
