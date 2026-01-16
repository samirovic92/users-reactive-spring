package com.sei.users.domain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    @Value("${token.secret}")
    private String tokenSecret;

    public String generateJwt(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Optional.ofNullable(tokenSecret)
                .map(String::getBytes)
                .map(Keys::hmacShaKeyFor)
                .orElseThrow(() -> new IllegalArgumentException("Token secret has not been set"));
    }
}
