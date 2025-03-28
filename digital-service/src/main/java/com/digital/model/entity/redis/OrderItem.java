package com.digital.model.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/28 15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String productName;

    private String productImage;

    private BigDecimal Price;

    private Integer quantity;

    private BigDecimal totalPrice;
}
