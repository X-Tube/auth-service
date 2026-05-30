package com.microservice.authservice.dto;

public record LoginRequestDTO(
        String email,
        String password
) {}
