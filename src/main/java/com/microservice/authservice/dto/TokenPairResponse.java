package com.microservice.authservice.dto;

public record TokenPairResponse(
        String accessToken,
        String refreshToken
) {
}
