package com.digital.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  17:01
 */
@Component
public class JwtUtil {

    private String secretKey = "digitalKids";

    /**
     * 获得jwt令牌
     * @param username 传入用户名
     * @return 返回令牌
     */
    public String generateToken(String username) {
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuer("digital.com")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return jwt;
    }

    /**
     * 从token提取个人信息
     * @param token 令牌
     * @return 信息
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 是否过期
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    /**
     * 令牌jwt是否有效
     * @param token
     * @param username
     * @return
     */
    public boolean validateToken(String token, String username) {
        return username.equals(getUsername(token)) && !isTokenExpired(token);
    }

}
