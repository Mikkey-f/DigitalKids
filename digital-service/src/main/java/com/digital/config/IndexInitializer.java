package com.digital.config;

import com.digital.model.entity.ParentingEncyclopedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  22:28
 */
@Component
public class IndexInitializer {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostConstruct
    public void initIndex() {

    }
}
