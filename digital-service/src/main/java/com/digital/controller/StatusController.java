package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.vo.tongue.TongueResult;
import com.digital.result.Result;
import com.digital.utils.MarkdownUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/23 17:03
 */
@RestController
public class StatusController {

    private static final Map<String, String> sessions = new ConcurrentHashMap<>();
    private static final Map<String, TongueResult> sessionsForTongue = new ConcurrentHashMap<>();
    public static void saveSession(String sessionId, String body) {
        sessions.put(sessionId, body);
    }

    public static void saveSessionForTongue(String sessionId, TongueResult tongueResult) {
        sessionsForTongue.put(sessionId, tongueResult);
    }

    /**
     * 智能体提问后，轮询这个接口得到答案
     * @param sessionId
     * @return
     */
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @GetMapping("/status/{sessionId}")
    public Result<String> getStatus(@PathVariable String sessionId) {
        String s = sessions.get(sessionId);
        if (s == null) {
            return Result.error(ResultErrorEnum.QUESTION_RESULT_NOT_FIND.getMessage());
        }
        s = MarkdownUtils.markdownToHtml(s);

        return Result.success(s);
    }

    /**
     * 轮询获得舌苔的分析结果
     * @param sessionId
     * @return
     */
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @GetMapping("/status/tongue/{sessionId}")
    public Result<TongueResult> getStatusForTongue(@PathVariable String sessionId) {
        TongueResult s = sessionsForTongue.get(sessionId);
        if (s == null) {
            return Result.error(ResultErrorEnum.QUESTION_RESULT_NOT_FIND.getMessage());
        }
        return Result.success(s);
    }
}
