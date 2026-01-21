package com.sei.users.presentation;

import com.sei.users.domain.UserService;
import com.sei.users.presentation.request.CreateUserRequest;
import com.sei.users.presentation.response.UserCreatedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
class UserControllerTest {
    @MockitoBean
    private UserService userService;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void should_create_users() {
        // Given
        var createUserRequest = new CreateUserRequest(
                "alice",
                "persona",
                "alice.persona@gamil.com",
                "12345678"
        );

        when(userService.createUser(any())).thenReturn(Mono.just(new UserCreatedResponse(
                UUID.randomUUID(),
                createUserRequest.firstName(),
                createUserRequest.lastName(),
                createUserRequest.email()
        )));

        // When + Then
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserCreatedResponse.class)
                .value(
                        response -> {
                            assertEquals(createUserRequest.firstName(), response.firstName());
                            assertEquals(createUserRequest.lastName(), response.lastName());
                        }
                );

    }

}