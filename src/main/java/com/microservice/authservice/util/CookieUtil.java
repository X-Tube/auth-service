package com.microservice.authservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {

    @Value("${security.jwt-expiration}")
    private long accessTokenDuration;

    @Value("${security.refresh-expiration}")
    private long refreshTokenDuration;

    public static final String ACCESS_TOKEN_COOKIE = "acessToken";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMillis(accessTokenDuration))
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenDuration))
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
