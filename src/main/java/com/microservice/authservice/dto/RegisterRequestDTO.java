package com.microservice.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        // @NotBlank(message = "The email address cannot be empty.")
        // @Email(message = "Invalid email format")
        String email,

        // @NotBlank(message = "The password cannot be empty")
        // @Size(min = 6, message = "The password must be at least 6 characters long")
        String password
) {}
