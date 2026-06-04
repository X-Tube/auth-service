package com.microservice.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Password is required")
        String email,
        @NotBlank
        String password
) {}
