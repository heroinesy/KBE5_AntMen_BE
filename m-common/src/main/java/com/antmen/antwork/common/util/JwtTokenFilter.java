package com.antmen.antwork.common.util;

import com.antmen.antwork.common.domain.entity.account.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
public class JwtTokenFilter extends GenericFilter {

    @Value("${JWT_SECRET}")
    private String secretkey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 헤더와 쿠키 둘 다에서 토큰 추출 시도
        String token = getTokenFromRequest(httpRequest);

        // favicon 요청은 무시
        String requestURI = httpRequest.getRequestURI();
        if ("/favicon.ico".equals(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (token != null) {
                if (!token.startsWith("Bearer ")) {
                    throw new AuthenticationServiceException("Bearer 형식 아닙니다.");
                }
                String jwtToken = token.substring(7);
                // token 검증 및 claims(payload) 추출
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretkey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                // JWT에서 사용자 정보 추출
                String userId = claims.getSubject();
                String userRoleString = (String) claims.get("userRole");

                // String을 UserRole enum으로 변환
                UserRole userRole = UserRole.valueOf(userRoleString);

                // ===== 핵심 변경 부분: AuthUserDto 객체 생성 =====
                AuthUserDto authUserDto = new AuthUserDto(userId, userRole);

                // Authentication 객체 생성 (AuthUserDto 사용)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(authUserDto, jwtToken, authUserDto.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }
    }

    // 헤더와 쿠키에서 토큰 추출하는 메서드
    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. Authorization 헤더에서 토큰 확인
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null) {
            return headerToken;
        }

        // 2. 쿠키에서 토큰 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth-token".equals(cookie.getName())) {  // 쿠키명은 실제 사용하는 이름으로 변경
                    String cookieValue = cookie.getValue();
                    try {
                        // URL 디코딩 추가
                        String decodedValue = java.net.URLDecoder.decode(cookieValue, "UTF-8");
                        return decodedValue;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return cookieValue; // 디코딩 실패시 원본 반환
                    }
                }
            }
        }

        return null;
    }
}
