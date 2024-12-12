package org.example.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.config.JwtConfig;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;

@Component
public class JwtUtil {
    private final Key key;
    private final Long expiration;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.trim().isEmpty() || "your-default-secret-key".equals(secret)) {
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            this.key = secretKey;
            String generatedSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Generated new secret key: " + generatedSecret);
        } else {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            this.key = Keys.hmacShaKeyFor(decodedKey);
        }
        this.expiration = jwtConfig.getExpiration();
    }

    public String generateToken(String subject, Integer userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key)
                .compact();
    }

    public String generateToken(User user, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getIdentityCard())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key)
                .compact();
    }

    public String generateAdminToken(String username, Integer adminId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", adminId);
        claims.put("role", "ADMIN");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 