package com.sei.users.domain;

import com.sei.users.infrastructure.UserEntity;
import com.sei.users.infrastructure.UserRepository;
import com.sei.users.presentation.request.CreateUserRequest;
import com.sei.users.presentation.response.UserCreatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserCreatedResponse> createUser(Mono<CreateUserRequest> createUserRequest) {
        return createUserRequest.mapNotNull(this::convertToEntity)
                .flatMap(userRepository::save)
                .mapNotNull(this::convertToUserResponse);
    }

    public Mono<UserCreatedResponse> getUserById(UUID userId) {
        return userRepository.findById(userId)
                .mapNotNull(this::convertToUserResponse)
                .onErrorMap(e -> new UserCreationException(e.getMessage()));
    }

    public Flux<UserCreatedResponse> findAllBy(int page, int limit) {
        var pageable = Pageable.ofSize(limit).withPage(page);
        return userRepository.findAllBy(pageable)
                .map(this::convertToUserResponse);
    }

    private UserEntity convertToEntity(CreateUserRequest createUserRequest) {
        var userEntity = new UserEntity();
        BeanUtils.copyProperties(createUserRequest, userEntity);
        userEntity.setPassword(passwordEncoder.encode(createUserRequest.password()));
        return userEntity;
    }

    private UserCreatedResponse convertToUserResponse(UserEntity userEntity) {
        return new UserCreatedResponse(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail()
        );
    }
}
