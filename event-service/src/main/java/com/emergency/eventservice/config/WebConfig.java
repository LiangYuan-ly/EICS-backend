package com.emergency.eventservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前工作目录下的 uploads 文件夹的绝对路径
        String uploadPath = System.getProperty("user.dir") + "/uploads/";

        // 确保路径以分隔符结尾
        if (!uploadPath.endsWith(File.separator)) {
            uploadPath += File.separator;
        }

        // 将请求路径 /uploads/** 映射到本地文件系统的 uploads 目录
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}