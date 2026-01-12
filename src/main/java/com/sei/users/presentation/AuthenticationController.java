package com.sei.users.presentation;

import com.sei.users.domain.AuthenticationService;
import com.sei.users.presentation.request.AuthenticationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Void>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequest) {
        return authenticationRequest.flatMap(request ->
                        authenticationService.authenticate(request.username(), request.password()))
                .map(this::buildAuthenticationRespone);
    }

    private ResponseEntity<Void> buildAuthenticationRespone(Map<String, String> response) {
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.get("token"))
                .header("userId", response.get("userId"))
                .build();
    }

}
