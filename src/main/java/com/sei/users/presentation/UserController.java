package com.sei.users.presentation;

import com.sei.users.presentation.request.CreateUserRequest;
import com.sei.users.presentation.response.UserCreatedResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Mono<ResponseEntity<UserCreatedResponse>> createUser(@RequestBody @Valid Mono<CreateUserRequest> request) {
        return request.map(
                req -> new UserCreatedResponse(
                        UUID.randomUUID(),
                        req.firstName(),
                        req.lastName(),
                        req.email()
                )
        ).map(ResponseEntity::ok);
    }

    @GetMapping("/{userId}")
    public Mono<UserCreatedResponse> getUser(@PathVariable UUID userId) {
        return Mono.just(
                new UserCreatedResponse(
                        userId,
                        "Samic",
                        "idrissi",
                        "sam@gmail.com"
                )
        );
    }

    @GetMapping
    public Flux<UserCreatedResponse> getAllUsers() {
        return Flux.just(
                new UserCreatedResponse(
                        UUID.randomUUID(),
                        "samir",
                        "idrissi",
                        "sam@gmail.com"
                ),
                new UserCreatedResponse(
                        UUID.randomUUID(),
                        "Jhon",
                        "Bob",
                        "jbob@gmail.com"
                )
        );
    }

}
