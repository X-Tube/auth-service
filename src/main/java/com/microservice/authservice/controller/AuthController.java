package com.microservice.authservice.controller;

import com.microservice.authservice.dto.RegisterRequestDTO;
import com.microservice.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterRequestDTO body){

        String message = authService.register(body);
        return ResponseEntity.ok(message);
    }
}
