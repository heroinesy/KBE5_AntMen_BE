package com.antmen.antwork.core.security;
















@Component
@RequiredArgsConstructor
public class AdminJwtTokenProvider {
    @Value("${JWT_SECRET}")
    private String secretKey;

    private Key key;

    private final long expiration = 1000 * 60 * 120; // 120분

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Long adminId) {
        Claims claims = Jwts.claims().setSubject(adminId.toString());
        claims.put("userRole", "ADMIN");
        claims.put("userId", adminId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getAdminId(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public long getExpiration() {
        return expiration;
    }
}
