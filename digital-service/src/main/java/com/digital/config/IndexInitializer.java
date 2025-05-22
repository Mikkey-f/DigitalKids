package com.digital.config;

import com.digital.model.entity.Product;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  22:28
 */
@Component
public class IndexInitializer {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostConstruct
    public void initIndex() throws IOException {
//        String indexName = "product";
//        // 检查索引是否已存在
//        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
//        boolean indexExists = elasticsearchOperations.getClient().indices().exists(getIndexRequest, RequestOptions.DEFAULT);
//        if (!indexExists) {
//            // 创建索引请求
//            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
//            // 获取索引映射
//            String mapping = elasticsearchOperations.getElasticsearchConverter()
//                    .getMappingContext()
//                    .getRequiredPersistentEntity(Product.class)
//                    .getIndexCoordinates()
//                    .getIndexName()
//                    .getMapping();
//            createIndexRequest.source(mapping, XContentType.JSON);
//            // 执行创建索引操作
//            CreateIndexResponse createIndexResponse = elasticsearchOperations.getClient()
//                    .indices()
//                    .create(createIndexRequest, RequestOptions.DEFAULT);
//            if (createIndexResponse.isAcknowledged()) {
//                System.out.println("索引创建成功");
//            } else {
//                System.out.println("索引创建失败");
//            }
//        } else {
//            System.out.println("索引已存在");
//        }
    }
}


