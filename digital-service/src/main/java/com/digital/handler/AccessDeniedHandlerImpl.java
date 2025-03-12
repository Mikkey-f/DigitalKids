package com.digital.handler;

import com.alibaba.fastjson.JSON;
import com.digital.enums.ResultErrorEnum;
import com.digital.result.Result;
import com.digital.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 没有权限访问就在这个处理器中被处理
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  18:55
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String jsonString = JSON.toJSONString(Result.error(ResultErrorEnum.NO_AUTH_ERROR.getMessage()));
        WebUtils.renderString(response, jsonString);

    }
}
