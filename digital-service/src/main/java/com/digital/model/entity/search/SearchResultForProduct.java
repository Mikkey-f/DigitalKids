package com.digital.model.entity.search;

import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/22 17:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultForProduct {
    private List<Product> productList;
    private long total;
}
