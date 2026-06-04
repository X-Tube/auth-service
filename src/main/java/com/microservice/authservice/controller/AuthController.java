package com.microservice.authservice.controller;

import com.microservice.authservice.dto.*;
import com.microservice.authservice.service.AuthService;

import com.microservice.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<TokenPairResponse> register (@Valid @RequestBody RegisterRequestDTO dto){

        TokenPairResponse tokens = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequestDTO dto){
        TokenPairResponse tokens = authService.login(dto);
        return ResponseEntity.ok(tokens);

    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairResponse> refresh(@Valid @RequestBody RefreshTokenRequestDTO dto){
        TokenPairResponse tokenPairResponse = authService.refreshToken(dto);

        return ResponseEntity.ok(tokenPairResponse);
    }

}
