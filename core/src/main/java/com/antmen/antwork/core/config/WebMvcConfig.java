package com.antmen.antwork.core.config;







@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SlowApiInterceptor slowApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(slowApiInterceptor)
                .excludePathPatterns("/api/v1/auth/**","/api/v1/admin/auth");
    }
}