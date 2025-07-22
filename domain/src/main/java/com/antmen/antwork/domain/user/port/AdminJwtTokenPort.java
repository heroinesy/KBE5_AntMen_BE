package com.antmen.antwork.domain.user.port;

public interface AdminJwtTokenPort {
    String createToken(Long adminId);
    boolean validateToken(String token);
    String getAdminId(String token);
    long getExpiration();
} 