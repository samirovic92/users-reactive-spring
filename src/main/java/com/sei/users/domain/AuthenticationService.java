package com.sei.users.domain;

import com.sei.users.infrastructure.UserEntity;
import com.sei.users.infrastructure.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private final ReactiveAuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Mono<Map<String, String>> authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .then(userRepository.findByEmail(username))
                .map(this::buildAuthenticationRespone);
    }

    private Map<String, String> buildAuthenticationRespone(UserEntity userEntity) {
        var userId = userEntity.getId().toString();
        return Map.of(
                "token", jwtService.generateJwt(userId),
                "userId", userId
        );
    }

}
