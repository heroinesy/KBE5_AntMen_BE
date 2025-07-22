package com.antmen.antwork.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class SlowApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        req.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {

        Object startTime = req.getAttribute("startTime");
        if (startTime == null) return;

        long duration = System.currentTimeMillis() - (long) startTime;

        if (duration > 1000) { // 1초 이상만 경고
            String method = req.getMethod();
            String uri = req.getRequestURI();
            String query = req.getQueryString() != null ? "?" + req.getQueryString() : "";
            String clientIp = req.getRemoteAddr();

            // Controller 메서드 정보
            String handlerInfo = "";
            if (handler instanceof org.springframework.web.method.HandlerMethod handlerMethod) {
                handlerInfo = handlerMethod.getBeanType().getSimpleName() + "#" +
                        handlerMethod.getMethod().getName();
            }

            log.warn("🚨 SLOW API [{}] {}ms | {} {}{} | IP={} | Handler={} ",
                    res.getStatus(),
                    duration,
                    method,
                    uri,
                    query,
                    clientIp,
                    handlerInfo
            );
        }
    }
}