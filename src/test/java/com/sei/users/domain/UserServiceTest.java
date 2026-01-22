package com.sei.users.domain;

import com.sei.users.infrastructure.UserEntity;
import com.sei.users.infrastructure.UserRepository;
import com.sei.users.presentation.request.CreateUserRequest;
import com.sei.users.presentation.response.UserCreatedResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void should_create_users() {
        // Given
        var createUserRequest = new CreateUserRequest(
                "alice",
                "persona",
                "alice.persona@gamil.com",
                "12345678"
        );
        var userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setFirstName("alice");
        userEntity.setLastName("persona");
        userEntity.setEmail("alice.persona@gamil.com");
        userEntity.setPassword("12345678");

        when(userRepository.save(any())).thenReturn(Mono.just(userEntity));
        when(passwordEncoder.encode(any())).thenReturn("12345678");

        // When
        var response = userService.createUser(Mono.just(createUserRequest));

        // Then
        StepVerifier.create(response)
                .expectNext(new UserCreatedResponse(
                        userEntity.getId(),
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        userEntity.getEmail()
                ))
                .verifyComplete();

    }
}