package com.sei.users.filter;


import com.sei.users.domain.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class JwtAuthentificationFilter implements WebFilter {
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var token = extractToken(exchange);

        if (Objects.isNull(token)) {
            return chain.filter(exchange);
        }
        return jwtService.validateToken(extractToken(exchange))
                .flatMap( isValidToken -> isValidToken
                        ? handleSuccessAuthentication(token, exchange, chain)
                        : handleFailureAuthentication(exchange)
                );
    }


    private String extractToken(ServerWebExchange exchange) {
        var token = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        if(Objects.nonNull(token) && token.contains("Bearer ")) {
            return token.substring(7).trim();
        }
        return null;
    }

    private Mono<Void> handleSuccessAuthentication(String token, ServerWebExchange exchange, WebFilterChain chain) {
        return  Mono.just(jwtService.getSubject(token))
                .map(subject -> new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList()))
                .flatMap( auth -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
    }

    private Mono<Void> handleFailureAuthentication(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return  exchange.getResponse().setComplete();
    }
}
