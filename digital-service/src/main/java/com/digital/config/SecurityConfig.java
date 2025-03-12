package com.digital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  09:51
 */
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Autowired
//    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    @Autowired
//    AccessDeniedHandlerImpl accessDeniedHandler;
//    @Autowired
//    AuthenticationEntryPointImpl authenticationEntryPoint;
//
//    @Autowired
//    private AuthenticationConfiguration authenticationConfiguration;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                //关闭csrf
//                .csrf().disable()
//                //不通过Session获取SecurityContext
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 对于登录接口 允许匿名访问
//                .antMatchers("/user/login","/user/registerInvite","/user/loginInvite","/order/notify","/user/register","/swagger/**","/swagger-resources/**","/swagger-ui/**", "/v3/**", "/error","/**/*.js","/**/*.css","/**/*.png", "/webjars","/favicon.ico","/doc.html","/websocket/*","/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").anonymous()
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();
//        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        //告诉security如何处理异常
//        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler);
//        http.cors();
//        return http.build();
//    }



//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception{
//        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
//        return authenticationManager;
//    }
}
