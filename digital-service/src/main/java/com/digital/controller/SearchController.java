package com.digital.controller;

import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.search.SearchResultForPar;
import com.digital.model.entity.User;
import com.digital.model.request.search.SearchAllParEncyReq;
import com.digital.model.request.search.SearchParEncyByStageReq;
import com.digital.model.vo.search.SearchVo;
import com.digital.model.vo.user.SearchUserVo;
import com.digital.result.Result;
import com.digital.service.UserService;
import com.digital.service.impl.ElasticsearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 搜索功能
     * @param searchReq
     * @return
     */
    @PostMapping("/search")
    public Result<List<SearchVo>> search(@RequestBody SearchAllParEncyReq searchReq) {
        SearchResultForPar searchResultForPar = elasticsearchService.searchEncyclopedia(
                searchReq.getKeyword(), -1, (int) (searchReq.getPageReq().getCurrent() - 1), (int) searchReq.getPageReq().getPageSize()
        );

        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResultForPar.getParentingEncyclopediaList();
        return Result.success(getSearchVoList(parentingEncyclopediaList));
    }

    /**
     * 根据阶段搜文章
     * @param searchParEncyByStageReq
     * @return
     */
    @PostMapping("/search/stage")
    public Result<List<SearchVo>> searchByStage(@RequestBody SearchParEncyByStageReq searchParEncyByStageReq) {
        SearchResultForPar searchResultForPar = elasticsearchService.searchEncyclopedia(
                searchParEncyByStageReq.getKeyword(), searchParEncyByStageReq.getStage(), (int)(searchParEncyByStageReq.getPageReq().getCurrent() - 1), (int) searchParEncyByStageReq.getPageReq().getPageSize()
        );

        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResultForPar.getParentingEncyclopediaList();

        return Result.success(getSearchVoList(parentingEncyclopediaList));
    }

    private List<SearchVo> getSearchVoList(List<ParentingEncyclopedia> parentingEncyclopediaList) {

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
                    searchVo.setCreateTime(parentingEncyclopedia.getCreateTime());
                    searchVoArrayList.add(searchVo);
                }
            }
        }
        return searchVoArrayList;
    }
}
