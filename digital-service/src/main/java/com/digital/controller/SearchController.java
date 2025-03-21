package com.digital.controller;

import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.SearchResult;
import com.digital.model.entity.User;
import com.digital.model.request.search.SearchReq;
import com.digital.model.vo.search.SearchVo;
import com.digital.model.vo.user.GetUserVo;
import com.digital.model.vo.user.SearchUserVo;
import com.digital.result.Result;
import com.digital.service.UserService;
import com.digital.service.impl.ElasticsearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:42
 */
@RestController
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private UserService userService;

    @PostMapping("/search")
    public Result<List<SearchVo>> search(@RequestBody SearchReq searchReq) {
        SearchResult searchResult = elasticsearchService.searchEncyclopedia(
                searchReq.getKeyword(), (int) (searchReq.getPageReq().getCurrent() - 1), (int) searchReq.getPageReq().getPageSize()
        );

        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResult.getParentingEncyclopediaList();
        List<Long> collectUserIds = parentingEncyclopediaList.stream().
                map(ParentingEncyclopedia::getUserId).
                collect(Collectors.toList());
        List<User> users = userService.listByIds(collectUserIds);
        ArrayList<SearchVo> searchVoArrayList = new ArrayList<>();
        for (User user : users) {
            for (ParentingEncyclopedia parentingEncyclopedia : parentingEncyclopediaList) {
                if (user.getId().equals(parentingEncyclopedia.getUserId())) {
                    SearchVo searchVo = new SearchVo();
                    SearchUserVo searchUserVo = new SearchUserVo();
                    BeanUtils.copyProperties(user, searchUserVo);
                    searchVo.setUser(searchUserVo);
                    searchVo.setContent(parentingEncyclopedia.getContent());
                    searchVo.setTitle(parentingEncyclopedia.getTitle());
                    searchVoArrayList.add(searchVo);
                }
            }
        }
        return Result.success(searchVoArrayList);
    }
}
