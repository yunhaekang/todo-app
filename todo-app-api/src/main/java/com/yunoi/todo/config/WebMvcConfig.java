package com.yunoi.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("http://localhost:3000",
                        "https://app.yunhaekang.com")   // Origin(출처): Protocol + Host + Port
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 메서드 작성
                .allowedHeaders("*")    // 모든 헤더 정보 허용
                .allowCredentials(true) // 인증에 관한 정보 허용
                .maxAge(MAX_AGE_SECS);
    }
}
