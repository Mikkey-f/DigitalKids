package com.digital.mapper.elasticsearch;

import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/22 17:25
 */
@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {
}
