package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.TopicConstant;
import com.digital.constant.UserConstant;
import com.digital.event.EventProducer;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.User;
import com.digital.model.entity.search.SearchResultForPar;
import com.digital.model.entity.search.SearchResultForProduct;
import com.digital.model.request.question.QuestionReq;
import com.digital.model.request.question.TalkAndForwardReq;
import com.digital.model.vo.question.TalkAndForwardVo;
import com.digital.model.vo.search.SearchVo;
import com.digital.model.vo.user.SearchUserVo;
import com.digital.result.Result;
import com.digital.service.UserService;
import com.digital.service.impl.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    private UserService userService;

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
                (int) (talkAndForwardReq.getPageReq().getCurrent() - 1), (int) talkAndForwardReq.getPageReq().getPageSize());
        SearchResultForProduct searchResultForProduct = elasticsearchService.searchProduct(talkAndForwardReq.getQuestion(),
                (int) (talkAndForwardReq.getPageReq().getCurrent() - 1), (int) talkAndForwardReq.getPageReq().getPageSize());
        TalkAndForwardVo talkAndForwardVo = new TalkAndForwardVo();
        List<Product> productList = searchResultForProduct.getProductList();
        Map<String, String> map = new ConcurrentHashMap<>();
        if (searchResultForProduct.getProductList().isEmpty()) {
            map.put("Product", "没有查询到");
        }

        if (searchResultForPar.getParentingEncyclopediaList().isEmpty()) {
            map.put("Baike", "没有查询到");
        }
        talkAndForwardVo.setMessage(map);
        List<ParentingEncyclopedia> parentingEncyclopediaList = searchResultForPar.getParentingEncyclopediaList();
        if (!parentingEncyclopediaList.isEmpty()) {
            talkAndForwardVo.setParentingEncyclopediaId(Math.toIntExact(searchResultForPar.getParentingEncyclopediaList().get(0).getId()));
        } else {
            talkAndForwardVo.setParentingEncyclopediaId(null);
        }

        if (!productList.isEmpty()) {
            talkAndForwardVo.setProductId(Math.toIntExact(searchResultForProduct.getProductList().get(0).getId()));
        } else {
            talkAndForwardVo.setProductId(null);
        }
        if (talkAndForwardVo.getParentingEncyclopediaId() == null && talkAndForwardVo.getProductId() == null) {
            map.put("TiShi", "没有搜索到任何内容");
            talkAndForwardVo.setMessage(map);
        }
        return Result.success(talkAndForwardVo);
    }
}
