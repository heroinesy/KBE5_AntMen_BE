package com.antmen.antwork.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public PasswordEncoder makePassword() {
        log.info("makePassword()");
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain myfilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                // Basic 비활성화
                // Basic 인증은 사용자이름과 비밀번호를 Base64로 인코딩하여 인증값으로 활용
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션방식을 비활성화
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 특정 Url 패턴에 대해서는 인증처리(Authentication 객체생성) 제외
                // .authorizeHttpRequests(a->a.requestMatchers("/*","/api/v1/auth/create","/api/v1/auth/login","/api/v1/auth/google/login").permitAll().anyRequest().authenticated())
                .authorizeHttpRequests(a -> a.requestMatchers("/**").permitAll().anyRequest().authenticated()) // 모든 경로로
                                                                                                               // 수정
                // UsernamePasswordAuthenticationFilter 이 클래스에서 폼로그인 인증을 처리
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(
                Arrays.asList(
                        "http://localhost:3000",
                        "http://localhost:3001",
                        "https://antmen.site",
                        "https://admin.antmen.site",
                        "https://*.antmen.site",
                        "https://api.antmen.site",
                        "https://api.antmen.site:*",
                        "https://api.antmen.site:909[0-3]"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
