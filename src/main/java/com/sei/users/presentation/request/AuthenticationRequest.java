package com.sei.users.presentation.request;


public record AuthenticationRequest(
        String username,
        String password
) { }

