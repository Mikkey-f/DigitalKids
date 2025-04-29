package com.digital.model.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 19:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Integer productId;


    private Integer quantity;

    private Boolean isSelected;
}
