package com.digital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  22:28
 */
@Component
public class IndexInitializer {
    @Autowired
    private ElasticSea elasticsearchOperations;

    @PostConstruct
    public void initIndex() {
        if (!elasticsearchOperations.indexExists(ParentingEncyclopedia.class)) {
            elasticsearchOperations.createIndex(ParentingEncyclopedia.class);
            elasticsearchOperations.putMapping(ParentingEncyclopedia.class);
        }
    }
}
