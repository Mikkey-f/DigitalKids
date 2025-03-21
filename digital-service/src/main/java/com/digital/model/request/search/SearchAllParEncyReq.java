package com.digital.model.request.search;

import com.digital.model.request.page.PageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:45
 */
@Data
public class SearchAllParEncyReq implements Serializable {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 页数
     */
    private PageReq pageReq;
    private static final long serialVersionUID = 1L;
}
