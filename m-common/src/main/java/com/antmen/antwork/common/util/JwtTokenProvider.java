package com.antmen.antwork.common.util;

import com.antmen.antwork.common.domain.entity.account.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey;

    private final int expriration;

    private Key SECRET_KEY;

    public JwtTokenProvider(@Value("${JWT_SECRET}")String secretKey, @Value("${jwt.expiration}") int expriration) {
        this.secretKey = secretKey;
        this.expriration = expriration;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
    }

    public String createToken(Long userId, String userRole) {
        // claims는 jwt토큰의 payload부분을 의미
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("userRole", userRole);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ expriration*60*1000L))
                .signWith(SECRET_KEY)
                .compact();

        return token;
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
