package com.microservice.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyRequestDTO(
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Verification code is required")
        String code
        ) {}
