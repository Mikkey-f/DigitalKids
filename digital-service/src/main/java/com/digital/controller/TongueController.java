package com.digital.controller;

import com.alibaba.fastjson.JSON;
import com.digital.annotation.AuthCheck;
import com.digital.constant.TopicConstant;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.event.EventProducer;
import com.digital.exception.BusinessException;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.entity.Product;
import com.digital.model.entity.search.SearchResultForPar;
import com.digital.model.entity.search.SearchResultForProduct;
import com.digital.model.request.question.QuestionReq;
import com.digital.model.request.question.TalkAndForwardReq;
import com.digital.model.vo.question.TalkAndForwardVo;
import com.digital.model.vo.tongue.TongueResult;
import com.digital.result.Result;
import com.digital.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/25 12:37
 */
@RestController
@Slf4j
public class TongueController {

    @Autowired
    private EventProducer eventProducer;

    private static final String ToGoUtl = "/status/tongue/{requestId}";
    private static final String Tongue_Diagnosis_IP = "127.0.0.1";
    private static final String Tongue_Diagnosis_PORT = "6666";


    private final WebClient webClientTongue;
    @Autowired
    private WebClient.Builder builder;

    public TongueController(WebClient.Builder webClientBuilder) {
        this.webClientTongue = webClientBuilder.baseUrl("http://" + Tongue_Diagnosis_IP + ":" + Tongue_Diagnosis_PORT).build();
    }
    /**
     * 检测舌头
     * @param file 舌头图片
     * @return
     */
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @PostMapping("/tongue")
    public Result<String> tongue(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(ResultErrorEnum.PLEASE_SELECT_IMAGE.getMessage());
        }

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

        try {
            // 将文件添加到 MultipartBodyBuilder 中
            multipartBodyBuilder.part("file", file.getResource());
        } catch (Exception e) {
            log.error("文件添加到 MultipartBodyBuilder 时出错", e);
            return Result.error(ResultErrorEnum.FILE_OPERATION_ERROR.getMessage());
        }

        Mono<ResponseEntity<TongueResult>> responseEntityMono = webClientTongue.method(HttpMethod.POST)
                .uri("/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .retrieve()
                .toEntity(TongueResult.class);

        String requestId = UUID.randomUUID().toString();
        try {
            ResponseEntity<TongueResult> response = responseEntityMono.block();
            if (response != null) {
                StatusController.saveSessionForTongue(requestId, response.getBody());
            }
            return Result.success("请求已被处理:" + ToGoUtl + "请求 ID：" + requestId);
        } catch (RuntimeException e) {
            if (e instanceof WebClientResponseException.UnprocessableEntity) {
                WebClientResponseException.UnprocessableEntity unprocessableEntity = (WebClientResponseException.UnprocessableEntity) e;
                log.error("422 Unprocessable Entity. Status code: {}", unprocessableEntity.getStatusCode());
                log.error("Error details: {}", unprocessableEntity.getResponseBodyAsString());
            } else if (e instanceof WebClientResponseException.BadRequest) {
                WebClientResponseException.BadRequest badRequest = (WebClientResponseException.BadRequest) e;
                log.error("400 Bad Request. Status code: {}", badRequest.getStatusCode());
                log.error("Error details: {}", badRequest.getResponseBodyAsString());
            } else {
                log.error("其他异常: {}", e.getMessage());
            }
            throw new BusinessException(ResultErrorEnum.SMART_AGENT_ERROR);
        }
    }
}
