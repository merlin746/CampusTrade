package com.example.schooltrade.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 工具类
 * 负责 Token 的生成、解析和校验
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expiration;

    /**
     * 构造函数注入配置
     * @param secret    Base64 编码的密钥
     * @param expiration Token 过期时间（毫秒）
     */
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") Long expiration) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    /**
     * 生成 Token
     * @param userId   用户ID
     * @param username 用户名
     * @param role     角色
     * @return JWT Token 字符串
     */
    public String generateToken(Long userId, String username, Integer role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 从 Token 中解析 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 校验 Token 是否有效
     * @return true=有效, false=过期或无效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** 从 Token 中提取用户ID */
    public Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    /** 从 Token 中提取用户名 */
    public String getUsername(String token) {
        return parseToken(token).get("username", String.class);
    }

    /** 从 Token 中提取角色 */
    public Integer getRole(String token) {
        return parseToken(token).get("role", Integer.class);
    }
}
