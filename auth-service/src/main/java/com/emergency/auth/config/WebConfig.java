package com.emergency.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = System.getProperty("user.dir") + "/static/avatar/";
        if (!uploadPath.endsWith(File.separator)) {
            uploadPath += File.separator;
        }
        registry.addResourceHandler("/static/avatar/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
