package com.antmen.antwork.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private final LastLoginUpdateInterceptor lastLoginUpdateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(lastLoginUpdateInterceptor)
                .addPathPatterns("/api/v1/**")           // v1 API 모두 포함
                .addPathPatterns("/customers/**")         // CustomerController 포함
                .addPathPatterns("/managers/**")          // 혹시 ManagerController도 있을 수 있으니
                .excludePathPatterns("/api/v1/auth/login", "/api/v1/auth/signup"); // 로그인/회원가입 제외
    }

}
