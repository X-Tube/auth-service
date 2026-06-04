package com.microservice.authservice.controller;

import com.microservice.authservice.dto.*;
import com.microservice.authservice.service.AuthService;

import com.microservice.authservice.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register (@Valid @RequestBody RegisterRequestDTO dto){

        String message = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/verify")
    public ResponseEntity<TokenPairResponse> verify(@Valid @RequestBody VerifyRequestDTO dto){
        TokenPairResponse tokens = authService.verifyUser(dto);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequestDTO dto){
            TokenPairResponse tokens = authService.login(dto);
        return ResponseEntity.ok(tokens);

    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@Valid @RequestBody ResendCodeRequestDTO dto) {

        String message = authService.resendVerificationCode(dto);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairResponse> refresh(@Valid @RequestBody RefreshTokenRequestDTO dto){
        TokenPairResponse tokenPairResponse = authService.refreshToken(dto);

        return ResponseEntity.ok(tokenPairResponse);
    }

}
