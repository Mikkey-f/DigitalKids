package com.digital.model.request.product;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 16:00
 */
@Data
public class ProductAddReq {

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

}
