package com.digital.controller;

import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.search.SearchResultForPar;
import com.digital.model.entity.User;
import com.digital.model.entity.search.SearchResultForProduct;
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
     * 百科搜索功能
     * @param searchReq
     * @return
     */
    @PostMapping("/parentingEncy/search")
    public Result<List<SearchVo>> searchForParentingEncy(@RequestBody SearchAllParEncyReq searchReq) {
        SearchResultForPar searchResultForPar = elasticsearchService.searchEncyclopedia(
                searchReq.getKeyword(), -1, (int) (searchReq.getPageReq().getCurrent() - 1), (int) searchReq.getPageReq().getPageSize()
        );

        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResultForPar.getParentingEncyclopediaList();
        if (parentingEncyclopediaList.isEmpty()) {
            return Result.error(ResultErrorEnum.NOT_SEARCH_CONTENT.getMessage());
        }
        return Result.success(getSearchVoList(parentingEncyclopediaList, ParentingEncyclopedia.class));
    }

    /**
     * 商品搜索功能
     * @param searchReq
     * @return
     */
    @PostMapping("/product/search")
    public Result<List<SearchVo>> searchForProduct(@RequestBody SearchAllParEncyReq searchReq) {
        SearchResultForProduct searchProduct = elasticsearchService.searchProduct(
                searchReq.getKeyword(), (int) (searchReq.getPageReq().getCurrent() - 1), (int) searchReq.getPageReq().getPageSize()
        );

        List<Product> productList = searchProduct.getProductList();
        if (productList.isEmpty()) {
            return Result.error(ResultErrorEnum.NOT_SEARCH_CONTENT.getMessage());
        }
        return Result.success(getSearchVoList(productList, Product.class));
    }

    /**
     * 根据阶段搜文章
     * @param searchParEncyByStageReq
     * @return
     */
    @PostMapping("/parentingEncy/search/stage")
    public Result<List<SearchVo>> searchByStage(@RequestBody SearchParEncyByStageReq searchParEncyByStageReq) {
        SearchResultForPar searchResultForPar = elasticsearchService.searchEncyclopedia(
                searchParEncyByStageReq.getKeyword(), searchParEncyByStageReq.getStage(), (int)(searchParEncyByStageReq.getPageReq().getCurrent() - 1), (int) searchParEncyByStageReq.getPageReq().getPageSize()
        );

        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResultForPar.getParentingEncyclopediaList();

        if (parentingEncyclopediaList.isEmpty()) {
            return Result.error(ResultErrorEnum.NOT_SEARCH_CONTENT.getMessage());
        }
        return Result.success(getSearchVoList(parentingEncyclopediaList, ParentingEncyclopedia.class));
    }

    private List<SearchVo> getSearchVoList(List<?> list, Class<?> elementType) {

        // 获取 List 元素的类型
        List<SearchVo> searchVoArrayList = null;
        if (elementType == ParentingEncyclopedia.class) {
            List<ParentingEncyclopedia> parentingEncyclopediaList = (List<ParentingEncyclopedia>) list;
            List<Long> collectUserIds = parentingEncyclopediaList.stream().
                    map(ParentingEncyclopedia::getUserId).
                    collect(Collectors.toList());
            List<User> users = userService.listByIds(collectUserIds);
            searchVoArrayList = new ArrayList<>();
            for (User user : users) {
                for (ParentingEncyclopedia parentingEncyclopedia : parentingEncyclopediaList) {
                    if (user.getId().equals(parentingEncyclopedia.getUserId())) {
                        SearchVo searchVo = new SearchVo();
                        SearchUserVo searchUserVo = new SearchUserVo();
                        BeanUtils.copyProperties(user, searchUserVo);
                        searchVo.setId(parentingEncyclopedia.getId());
                        searchVo.setUser(searchUserVo);
                        searchVo.setContent(parentingEncyclopedia.getContent());
                        searchVo.setTitle(parentingEncyclopedia.getTitle());
                        searchVo.setCreateTime(parentingEncyclopedia.getCreateTime());
                        searchVoArrayList.add(searchVo);
                    }
                }
            }
        } else if (elementType == Product.class) {
            List<Product> productList = (List<Product>) list;
            searchVoArrayList = new ArrayList<>();
            for (Product product : productList) {
                SearchVo searchVo = new SearchVo();
                searchVo.setId(Long.valueOf(product.getId()));
                searchVo.setContent(product.getDetail());
                searchVo.setTitle(product.getName());
                searchVo.setPrice(product.getPrice());
                searchVo.setCreateTime(product.getCreateTime());
                searchVo.setMainImage(product.getMainImage());
                searchVoArrayList.add(searchVo);
            }
        }

        return searchVoArrayList;
    }


}
