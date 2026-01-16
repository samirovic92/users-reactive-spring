package com.sei.users.presentation.response;

import java.util.Map;

public record AuthenticationResponse(
        String userId,
        String token
) {
}
