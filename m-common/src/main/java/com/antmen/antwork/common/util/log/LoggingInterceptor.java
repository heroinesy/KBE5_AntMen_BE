package com.antmen.antwork.common.util.log;

/*
=====================================================
🧹 LoggingInterceptor 주석처리됨
=====================================================

복잡한 HTTP 요청/응답 로깅 인터셉터를 주석처리했습니다.
개발 중에는 간단한 로그만 사용하고,
필요시 주석을 해제하여 상세한 HTTP 로깅을 활성화할 수 있습니다.

=====================================================
*/

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    
    // 🧹 주석처리: 복잡한 HTTP 로깅 대신 간단하게
    /*
    private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("ACCESS_LOG");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청 시작 시간 기록
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        
        // RequestId 생성 및 MDC 설정
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);
        
        // JWT에서 userId 추출 (있다면)
        String userId = extractUserIdFromToken(request);
        if (userId != null) {
            MDC.put("userId", userId);
        }
        
        // Access 로그에 필요한 MDC 설정
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        MDC.put("userAgent", request.getHeader("User-Agent"));
        
        // IP 주소 기록 (프록시 고려)
        String clientIp = getClientIpAddress(request);
        MDC.put("clientIp", clientIp);
        
        log.info("🚀 HTTP Request: {} {} from {} [{}]", 
            request.getMethod(), request.getRequestURI(), clientIp, requestId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            // 응답 시간 계산
            Long startTime = (Long) request.getAttribute("startTime");
            long responseTime = startTime != null ? System.currentTimeMillis() - startTime : 0;
            
            // MDC에 추가 정보 설정
            MDC.put("status", String.valueOf(response.getStatus()));
            MDC.put("responseTime", String.valueOf(responseTime));
            
            // Access 로그 기록
            ACCESS_LOGGER.info("HTTP {} {} {} {}ms {} {}", 
                request.getMethod(), 
                request.getRequestURI(),
                response.getStatus(),
                responseTime,
                MDC.get("userId") != null ? "user:" + MDC.get("userId") : "anonymous",
                getClientIpAddress(request)
            );
            
            // 에러 발생 시 추가 로그
            if (ex != null) {
                log.error("❌ HTTP Request Failed: {} {} - {}", 
                    request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
            }
            
            // 느린 요청 경고 (30초 이상)
            if (responseTime > 30000) {
                log.warn("🐌 Slow Request: {} {} took {}ms [requestId:{}]", 
                    request.getMethod(), request.getRequestURI(), responseTime, MDC.get("requestId"));
            }
            
        } finally {
            // MDC 정리
            MDC.clear();
        }
    }
    
    JWT 토큰에서 userId 추출
    private String extractUserIdFromToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                // JWT 파싱 로직 (실제 JWT 라이브러리 사용 필요)
                // 여기서는 간단히 null 반환
                return null;
            }
        } catch (Exception e) {
            log.debug("JWT 토큰 파싱 실패: {}", e.getMessage());
        }
        return null;
    }
    
    실제 클라이언트 IP 주소 추출 (프록시/로드밸런서 고려)
    private String getClientIpAddress(HttpServletRequest request) {
        String[] ipHeaders = {
            "X-Forwarded-For",
            "X-Real-IP", 
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED"
        };
        
        for (String header : ipHeaders) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For는 쉼표로 구분된 여러 IP를 포함할 수 있음
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
    */
    
    // 🧹 간단한 HTTP 로깅 (개발 편의성)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("HTTP Request: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
} 