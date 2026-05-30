package com.microservice.authservice.dto;

public record ValidateTokenResponseDTO(
        String userId,
        String email,
        String role
) {}
