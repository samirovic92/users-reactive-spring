package com.sei.users.presentation;

import com.sei.users.domain.AuthenticationService;
import com.sei.users.presentation.request.AuthenticationRequest;
import com.sei.users.presentation.response.AuthenticationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public Mono<ResponseEntity<AuthenticationResponse>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequest) {
        return authenticationRequest.flatMap(request ->
                        authenticationService.authenticate(request.username(), request.password()))
                .map(response -> new AuthenticationResponse(response.get("userId"), response.get("token")))
                .map(ResponseEntity::ok);
                //.onErrorReturn(BadCredentialsException.class, ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                //.onErrorReturn(Exception.class, ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


}
