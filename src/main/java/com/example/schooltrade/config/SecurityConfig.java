package com.example.schooltrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置
 * 配置权限规则、密码加密器、JWT 过滤器的注册顺序
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（前后端分离 + JWT 不需要）
            .csrf(csrf -> csrf.disable())
            // 无状态 Session（JWT 自带状态）
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求权限
            .authorizeHttpRequests(auth -> auth
                // 公开接口: 注册、登录、文件访问、API文档
                .requestMatchers(
                    "/api/auth/register", "/api/auth/login",
                    "/uploads/**", "/doc.html", "/v3/api-docs/**",
                    "/swagger-ui/**", "/webjars/**"
                ).permitAll()
                // 浏览接口无需登录（查看商品、分类、评价、用户信息）
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/goods", "/api/goods/{id}", "/api/goods/{id}/**",
                    "/api/category/**",
                    "/api/review/goods/**", "/api/review/user/**", "/api/review/stats/**",
                    "/api/user/{id}"
                ).permitAll()
                // 管理员专属接口
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // 分类管理需要管理员
                .requestMatchers(
                    HttpMethod.POST, "/api/category"
                ).hasRole("ADMIN")
                .requestMatchers(
                    HttpMethod.PUT, "/api/category/{id}"
                ).hasRole("ADMIN")
                .requestMatchers(
                    HttpMethod.DELETE, "/api/category/{id}"
                ).hasRole("ADMIN")
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            // JWT 过滤器在用户名密码认证之前执行
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * BCrypt 密码编码器
     * 用于注册时加密密码、登录时比对密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
