package com.sei.users.domain;

import com.sei.users.infrastructure.UserEntity;
import com.sei.users.infrastructure.UserRepository;
import com.sei.users.presentation.request.CreateUserRequest;
import com.sei.users.presentation.response.UserCreatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserCreatedResponse> createUser(Mono<CreateUserRequest> createUserRequest) {
        return createUserRequest.mapNotNull(this::convertToEntity)
                .flatMap(userRepository::save)
                .mapNotNull(this::convertToUserResponse);
    }

    public Mono<UserCreatedResponse> getUserById(UUID userId) {
        return userRepository.findById(userId)
                .mapNotNull(this::convertToUserResponse);
    }

    private UserEntity convertToEntity(CreateUserRequest createUserRequest) {
        var userEntity = new UserEntity();
        BeanUtils.copyProperties(createUserRequest, userEntity);
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
