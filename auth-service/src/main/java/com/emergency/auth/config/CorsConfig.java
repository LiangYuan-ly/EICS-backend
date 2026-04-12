package com.emergency.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口
                .allowedOriginPatterns("*") // 允许所有来源（可用逗号分隔指定前端地址，如 "http://localhost:5173"）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方式
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true) // 允许携带 Cookie/凭证
                .maxAge(3600); // 预检请求的有效期（秒）
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        String dirPath = System.getProperty("user.dir") + "/static/avatar/";
        registry.addResourceHandler("/static/avatar/**")
                .addResourceLocations("file:" + dirPath);
    }
}
