package com.digital.filter;

import com.alibaba.fastjson.JSON;
import com.digital.config.JwtConfig;
import com.digital.enums.ResultErrorEnum;
import com.digital.result.Result;
import com.digital.service.impl.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  18:51
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtConfig jwtConfig;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1 获取token
        String token = parseJwt(request);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        Date expirationDateFromToken = jwtConfig.getExpirationDateFromToken(token);
        // 判断token是否过期
        if (jwtConfig.isTokenExpired(expirationDateFromToken)) {
            returnJson(response, JSON.toJSONString(ResultErrorEnum.TOKEN_EXPIRED));
            return;
        }

        // 2 解析 token
        String usernameFromToken;
        usernameFromToken = jwtConfig.getUsernameFromToken(token);
        if (!StringUtils.hasText(usernameFromToken)) {
            returnJson(response, JSON.toJSONString(ResultErrorEnum.TOKEN_ERROR));
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(usernameFromToken);

        // 3 获取 userId 用户信息
        if (Objects.isNull(userDetails)) {
            returnJson(response, JSON.toJSONString(ResultErrorEnum.TOKEN_ERROR));
            return;
        }

        // 4 封装 Authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 5 存入 SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 放行
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void returnJson(ServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // 检查响应是否已经提交
        if (response.isCommitted()) {
            log.warn("Response already committed");
            return;
        }

        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            log.error("Response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
