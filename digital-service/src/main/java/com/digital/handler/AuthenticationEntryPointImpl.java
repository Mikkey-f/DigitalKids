package com.digital.handler;

import com.alibaba.fastjson.JSON;
import com.digital.enums.ResultErrorEnum;
import com.digital.result.Result;
import com.digital.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当用户没有认证成功时，使用这个模块解决
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  19:01
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String jsonString = JSON.toJSONString(Result.error(ResultErrorEnum.NO_AUTH_ERROR.getMessage()));
        WebUtils.renderString(response, jsonString);
    }
}
