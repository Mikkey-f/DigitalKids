package com.digitalkid.mcp.mcp.service;

import com.digitalkid.mcp.model.entity.ParentingEncyclopedia;
import com.digitalkid.mcp.model.request.AddParentingEncyclopediaReq;
import com.digitalkid.mcp.service.ParentingEncyclopediaService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 20:59
 */
@Service
public class ParentingEncyclopediaQueryService {

    @Resource
    private ParentingEncyclopediaService parentingEncyclopediaService;

    @Bean
    public Function<String, List<ParentingEncyclopedia>> findParentingEncyclopediaByTitle() {
        return title -> parentingEncyclopediaService.findParentingEncyclopediaByTitle(title);
    }

    @Bean
    public Function<String, List<ParentingEncyclopedia>> findParentingEncyclopediaByAuthor() {
        return author -> parentingEncyclopediaService.findParentingEncyclopediaByTitle(author);
    }

    @Bean
    public Function<Integer, Integer> deleteEncyclopediaById() {
        return id -> parentingEncyclopediaService.deleteEncyclopediaById(id);
    }

    @Bean
    public Function<AddParentingEncyclopediaReq, Integer> createEncyclopedia() {
        return req -> parentingEncyclopediaService.createEncyclopedia(req);
    }

}
