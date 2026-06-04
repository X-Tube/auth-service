package com.microservice.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank(message = "A refresh token is required")
        String refreshToken
) {}
