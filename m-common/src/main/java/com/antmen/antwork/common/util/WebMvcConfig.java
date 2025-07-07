package com.antmen.antwork.common.util;

// import com.antmen.antwork.common.util.log.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    // ================================================
    // 🧹 로깅 인터셉터 주석처리 (복잡한 로그 대신 간단하게)
    // ================================================
    
    /*
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**") // API 요청만 로깅
                .excludePathPatterns(
                    "/api/v1/docs/**",      // Swagger 문서 제외
                    "/api/actuator/**",     // Actuator 제외
                    "/api/health/**"        // Health Check 제외
                );
    }
    */
} 