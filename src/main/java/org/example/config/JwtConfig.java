package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret:your-default-secret-key}")
    private String secret;

    @Value("${jwt.expiration:86400}")  // 默认24小时
    private Long expiration;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }
} 