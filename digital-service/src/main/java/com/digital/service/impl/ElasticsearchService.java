package com.digital.service.impl;

import com.digital.mapper.elasticsearch.ParentingEncyclopediaRepository;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
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
    private ElasticsearchRestTemplate elasticsearchTemplate;

    public void saveEncyclopedia(ParentingEncyclopedia parentingEncyclopedia) {
        parentingEncyclopediaRepository.save(parentingEncyclopedia);
    }

    public void deleteEncyclopedia(Integer id) {
        parentingEncyclopediaRepository.deleteById(id);
    }

    public SearchResult searchEncyclopedia(String keyword, int current, int size) {
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
        return new SearchResult(list, totalHits);
    }
}
