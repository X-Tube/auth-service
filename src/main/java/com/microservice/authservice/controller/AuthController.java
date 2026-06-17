package com.microservice.authservice.controller;

import com.microservice.authservice.dto.*;
import com.microservice.authservice.service.AuthService;

import com.microservice.authservice.service.JwtService;
import com.microservice.authservice.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register (@Valid @RequestBody RegisterRequestDTO dto){
        String message = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerifyRequestDTO dto){
        TokenPairResponse tokens = authService.verifyUser(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(tokens.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(tokens.refreshToken()).toString())
                .body("User successfully verified!");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO dto){
            TokenPairResponse tokens = authService.login(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(tokens.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(tokens.refreshToken()).toString())
                .body("Login successful!");
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@Valid @RequestBody ResendCodeRequestDTO dto) {

        String message = authService.resendVerificationCode(dto);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue(name = CookieUtil.REFRESH_TOKEN_COOKIE, required = false) String refreshToken){
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No active session. Please log in again");
        }

        try {
            TokenPairResponse tokens = authService.refreshToken(refreshToken);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(tokens.accessToken()).toString())
                    .header(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(tokens.refreshToken()).toString())
                    .body("Tokens successfully refreshed!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteCookie(CookieUtil.ACCESS_TOKEN_COOKIE).toString())
                    .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteCookie(CookieUtil.REFRESH_TOKEN_COOKIE).toString())
                    .body("Your session has expired. Please log in again.");
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteCookie(CookieUtil.ACCESS_TOKEN_COOKIE).toString())
                .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteCookie(CookieUtil.REFRESH_TOKEN_COOKIE).toString())
                .body("Logged out!");
    }

}
