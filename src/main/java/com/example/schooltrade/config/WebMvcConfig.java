package com.example.schooltrade.config;

import com.example.schooltrade.interceptor.UserIdInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * WebMVC 配置
 * - 注册 UserIdInterceptor，使 Controller 可通过 @RequestAttribute("userId") 获取当前用户ID
 * - 映射 /uploads/** 到本地文件系统
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserIdInterceptor userIdInterceptor;

    @Value("${file.upload-path}")
    private String uploadPath;

    public WebMvcConfig(UserIdInterceptor userIdInterceptor) {
        this.userIdInterceptor = userIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 将当前用户ID注入到所有 /api/** 请求的属性中
        registry.addInterceptor(userIdInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File(uploadPath).getAbsolutePath();
        if (!absolutePath.endsWith(File.separator)) {
            absolutePath += File.separator;
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath);
    }
}
