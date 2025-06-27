package com.antmen.antwork.common.util;

import com.antmen.antwork.common.service.serviceAccount.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LastLoginUpdateInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // JWT 토큰에서 사용자 ID 추출하여 lastLoginAt 업데이트
        updateLastLoginFromToken(request);
        return true;
    }

    private void updateLastLoginFromToken(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token != null && jwtTokenProvider.isValidToken(token)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                if (userId != null) {
                    userService.updateLastLoginAsync(userId);
                }
            }
        } catch (Exception e) {
            log.debug("JWT 토큰에서 사용자 ID 추출 실패", e);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 헤더에서 토큰 확인
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2. 쿠키에서 토큰 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }



}
