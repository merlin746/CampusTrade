package com.example.schooltrade.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户ID拦截器
 * 从 Spring Security 上下文中提取当前登录用户的ID
 * 并设置为请求属性 "userId"，供 Controller 通过 @RequestAttribute 获取
 */
@Component
public class UserIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long userId) {
            request.setAttribute("userId", userId);
        }
        return true;
    }
}
