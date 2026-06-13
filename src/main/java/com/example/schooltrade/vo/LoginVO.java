package com.example.schooltrade.vo;

/**
 * 登录成功响应
 */
public class LoginVO {

    private Long userId;
    private String username;
    private String avatar;
    private Integer role;       // 0=用户, 1=管理员
    private String token;       // JWT Token
    private String tokenHead;   // Token 前缀 "Bearer "

    public LoginVO() {}

    public LoginVO(Long userId, String username, String avatar, Integer role, String token, String tokenHead) {
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.role = role;
        this.token = token;
        this.tokenHead = tokenHead;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenHead() { return tokenHead; }
    public void setTokenHead(String tokenHead) { this.tokenHead = tokenHead; }

}