package com.sei.users.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    @Value("${token.secret}")
    private String tokenSecret;

    public String generateToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(getSecretKey())
                .compact();
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.just(token)
                .map(this::getClaims)
                .map( claims ->  !claims.getExpiration().before(new Date()))
                .onErrorReturn(false);
    }

    public Object getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Optional.ofNullable(tokenSecret)
                .map(String::getBytes)
                .map(Keys::hmacShaKeyFor)
                .orElseThrow(() -> new IllegalArgumentException("Token secret has not been set"));
    }
}
