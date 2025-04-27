package com.digital.service.impl;

import com.digital.mapper.elasticsearch.ParentingEncyclopediaRepository;
import com.digital.mapper.elasticsearch.ProductRepository;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.search.SearchResultForPar;
import com.digital.model.entity.search.SearchResultForProduct;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:21
 */
@Service
public class ElasticsearchService {

    @Autowired
    private ParentingEncyclopediaRepository parentingEncyclopediaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    public void saveEncyclopedia(ParentingEncyclopedia parentingEncyclopedia) {
        parentingEncyclopediaRepository.save(parentingEncyclopedia);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    public void deleteEncyclopedia(Integer id) {
        parentingEncyclopediaRepository.deleteById(id);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public SearchResultForPar searchEncyclopedia(String keyword, int stage, int current, int size) {
        // 构建一个NativeSearchQuery并添加分页条件、排序条件以及高光区域
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withPageable(PageRequest.of(current, size))
                .withHighlightFields(//高亮显示
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<ParentingEncyclopedia> searchHits = elasticsearchTemplate.search(nativeSearchQuery, ParentingEncyclopedia.class);
        long totalHits = searchHits.getTotalHits();

        // 遍历搜索结果设置帖子的各个参数
        List<ParentingEncyclopedia> list = new ArrayList<>();
        if (searchHits.getTotalHits() != 0) {
            for (SearchHit<ParentingEncyclopedia> searchHit : searchHits) {
                if (stage != -1) {
                    if (!searchHit.getContent().getStage().equals(stage)) {
                        continue;
                    }
                }
                ParentingEncyclopedia post = new ParentingEncyclopedia();
                post.setId(searchHit.getContent().getId());
                post.setUserId(searchHit.getContent().getUserId());
                post.setTitle(searchHit.getContent().getTitle());
                post.setContent(searchHit.getContent().getContent());
                String createTime = searchHit.getContent().getCreateTime().toString();
                String updateTime = searchHit.getContent().getUpdateTime().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                Date date = null;
                Date date1 = null;
                try {
                    date = (Date) sdf.parse(createTime);
                    date1 = (Date) sdf.parse(updateTime);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                post.setCreateTime(date);
                post.setUpdateTime(date1);

                // 获得刚刚构建的高光区域，填到帖子的内容和标题上
                List<String> contentField = searchHit.getHighlightFields().get("content");
                if (contentField != null) {
                    post.setContent(contentField.get(0));
                }
                List<String> titleField = searchHit.getHighlightFields().get("title");
                if (titleField != null) {
                    post.setTitle(titleField.get(0));
                }
                list.add(post);
            }
        }
        return new SearchResultForPar(list, totalHits);
    }

    public SearchResultForProduct searchProduct(String keyword, int current, int size) {
        // 构建一个NativeSearchQuery并添加分页条件、排序条件以及高光区域
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "detail", "subtitle"))
                .withPageable(PageRequest.of(current, size))
                .withHighlightFields(//高亮显示
                        new HighlightBuilder.Field("name").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("detail").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("subtitle").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<Product> searchHits = elasticsearchTemplate.search(nativeSearchQuery, Product.class);
        long totalHits = searchHits.getTotalHits();

        // 遍历搜索结果设置帖子的各个参数
        List<Product> list = new ArrayList<>();
        if (searchHits.getTotalHits() != 0) {
            for (SearchHit<Product> searchHit : searchHits) {
                Product product = new Product();
                product.setId(searchHit.getContent().getId());
                product.setName(searchHit.getContent().getName());
                product.setDetail(searchHit.getContent().getDetail());
                product.setPrice(searchHit.getContent().getPrice());
                product.setMainImage(searchHit.getContent().getMainImage());
                String createTime = searchHit.getContent().getCreateTime().toString();
                String updateTime = searchHit.getContent().getUpdateTime().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                Date date = null;
                Date date1 = null;
                try {
                    date = (Date) sdf.parse(createTime);
                    date1 = (Date) sdf.parse(updateTime);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                product.setCreateTime(date);
                product.setUpdateTime(date1);

                // 获得刚刚构建的高光区域，填到帖子的内容和标题上
                List<String> contentField = searchHit.getHighlightFields().get("name");
                if (contentField != null) {
                    product.setName(contentField.get(0));
                }
                List<String> titleField = searchHit.getHighlightFields().get("detail");
                if (titleField != null) {
                    product.setDetail(titleField.get(0));
                }
                List<String> subtitleField = searchHit.getHighlightFields().get("subtitle");
                if (titleField != null) {
                    product.setDetail(subtitleField.get(0));
                }
                list.add(product);
            }
        }
        return new SearchResultForProduct(list, totalHits);
    }
}
