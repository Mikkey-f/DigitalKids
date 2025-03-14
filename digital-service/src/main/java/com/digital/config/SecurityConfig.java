package com.digital.config;

import com.digital.filter.JwtAuthenticationTokenFilter;
import com.digital.handler.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  09:51
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
//
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
<<<<<<< HEAD
                .antMatchers("/user/login","/user/register","/user/loginInvite","/swagger/**","/swagger-resources/**","/swagger-ui/**", "/v3/**", "/error","/**/*.js","/**/*.css","/**/*.png", "/webjars","/favicon.ico","/doc.html","/websocket/*","/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").anonymous()
=======
                .antMatchers("/user/login","/user/register", "/guest/login","/user/loginInvite","/swagger/**","/swagger-resources/**","/swagger-ui/**", "/v3/**", "/error","/**/*.js","/**/*.css","/**/*.png", "/webjars","/favicon.ico","/doc.html","/websocket/*","/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").anonymous()
>>>>>>> 3e1045ede3b7b3d2e0b41ee8184db19d5df6faad
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //告诉security如何处理异常
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.cors();
        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }
}
