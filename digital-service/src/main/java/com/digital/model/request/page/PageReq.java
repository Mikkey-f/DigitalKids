package com.digital.model.request.page;

import com.digital.constant.CommonConstant;
import lombok.Data;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-16  15:41
 */
@Data
public class PageReq {
    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
