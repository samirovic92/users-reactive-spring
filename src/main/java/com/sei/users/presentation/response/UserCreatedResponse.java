package com.sei.users.presentation.response;

import java.util.UUID;

public record UserCreatedResponse(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
