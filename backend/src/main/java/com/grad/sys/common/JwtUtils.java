package com.grad.sys.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    // 实际项目中应配置在 application.yml
    private static final String SECRET = "GradSysSecretKeyForJwtAuthenticationVeryLong";
    private static final long EXPIRE = 1000 * 60 * 60 * 24; // 24小时

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
