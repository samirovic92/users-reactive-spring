package com.sei.users.presentation;

import com.sei.users.domain.UserService;
import com.sei.users.presentation.request.CreateUserRequest;
import com.sei.users.presentation.response.UserCreatedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserCreatedResponse>> createUser(@RequestBody @Valid Mono<CreateUserRequest> request) {
        return this.userService.createUser(request)
                .map(item-> ResponseEntity.status(HttpStatus.CREATED).body(item));
    }

    @PreAuthorize("authentication.principal.equals(#userId.toString())")
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserCreatedResponse>> getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Flux<UserCreatedResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return userService.findAllBy(page, limit);
    }

}
