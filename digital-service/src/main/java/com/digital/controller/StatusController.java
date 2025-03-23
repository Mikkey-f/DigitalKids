package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.RoleEnum;
import com.digital.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    public static void saveSession(String sessionId, String body) {
        sessions.put(sessionId, body);
    }

    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @GetMapping("/status/{sessionId}")
    public Result<String> getStatus(@PathVariable String sessionId) {
        String s = sessions.get(sessionId);
        if (s == null) {
            Result.error(ResultErrorEnum.QUESTION_RESULT_NOT_FIND.getMessage());
        }

        return Result.success(s);
    }
}
