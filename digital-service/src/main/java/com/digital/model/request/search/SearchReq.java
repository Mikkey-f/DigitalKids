package com.digital.model.request.search;

import com.digital.model.request.page.PageReq;
import lombok.Data;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:45
 */
@Data
public class SearchReq {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 页数
     */
    private PageReq pageReq;
}
