package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.TopicConstant;
import com.digital.constant.UserConstant;
import com.digital.event.EventProducer;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.search.SearchResultForPar;
import com.digital.model.entity.search.SearchResultForProduct;
import com.digital.model.request.question.QuestionReq;
import com.digital.model.request.question.TalkAndForwardReq;
import com.digital.model.vo.question.TalkAndForwardVo;
import com.digital.result.Result;
import com.digital.service.impl.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/23 14:20
 */
@Slf4j
@RestController
public class QuestionController {

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private ElasticsearchService elasticsearchService;

    private static final String ToGoUtl = "/status/{requestId}";

    /**
     * 智能体提问，返回html
     * @param questionReq
     * @return
     */
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @PostMapping("/question")
    public Result<String> question(@RequestBody QuestionReq questionReq) {
        String requestId = UUID.randomUUID().toString();
        String message = requestId + ":" + questionReq.getQuestion();
        eventProducer.fireEventByQuestion(TopicConstant.TOPIC_QUESTION, message);
        return Result.success("请求已接收，正在处理，请轮询查询:" + ToGoUtl + "请求 ID：" + requestId);
    }

    /**
     * 用户跳转页面接口
     * @param talkAndForwardReq
     * @return
     */
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @PostMapping("/talk")
    public Result<TalkAndForwardVo> talkAndForward(@RequestBody TalkAndForwardReq talkAndForwardReq) {
        SearchResultForPar searchResultForPar = elasticsearchService.searchEncyclopedia(talkAndForwardReq.getQuestion(), -1,
                Math.toIntExact(talkAndForwardReq.getPageReq().getCurrent()), Math.toIntExact(talkAndForwardReq.getPageReq().getPageSize()));
        SearchResultForProduct searchResultForProduct = elasticsearchService.searchProduct(talkAndForwardReq.getQuestion(), -1,
                Math.toIntExact(talkAndForwardReq.getPageReq().getCurrent()), Math.toIntExact(talkAndForwardReq.getPageReq().getPageSize()));
        TalkAndForwardVo talkAndForwardVo = new TalkAndForwardVo();
        Map<String, String> map = new ConcurrentHashMap<>();
        if (searchResultForProduct.getProductList() == null) {
            map.put("Product", "没有查询到");
        }

        if (searchResultForPar.getParentingEncyclopediaList() == null) {
            map.put("Baike", "没有查询到");
        }
        talkAndForwardVo.setMessage(map);
        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResultForPar.getParentingEncyclopediaList();
        if (parentingEncyclopediaList != null) {
            talkAndForwardVo.setParentingEncyclopediaId(Math.toIntExact(searchResultForPar.getParentingEncyclopediaList().get(0).getId()));
        } else {
            talkAndForwardVo.setParentingEncyclopediaId(null);
        }

        List<Product> productList = searchResultForProduct.getProductList();
        if (productList != null) {
            talkAndForwardVo.setProductId(Math.toIntExact(searchResultForProduct.getProductList().get(0).getId()));
        } else {
            talkAndForwardVo.setProductId(null);
        }

        return Result.success(talkAndForwardVo);
    }
}
