package com.digital.controller;

import com.digital.service.impl.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:42
 */
@Controller
public class SearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/search")
    public String search() {}
}
