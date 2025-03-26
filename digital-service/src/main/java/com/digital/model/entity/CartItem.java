package com.digital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 19:22
 */
@Data
@AllArgsConstructor
public class CartItem {

    private Integer quantity;

    private Boolean isSelected;
}
