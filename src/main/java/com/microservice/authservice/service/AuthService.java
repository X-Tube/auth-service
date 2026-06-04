package com.microservice.authservice.service;

import com.microservice.authservice.dto.*;
import com.microservice.authservice.model.User;
import com.microservice.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public TokenPairResponse register(RegisterRequestDTO dto) {

        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenPairResponse(accessToken, refreshToken);
    }

    public TokenPairResponse login(LoginRequestDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenPairResponse(accessToken, refreshToken);

    }

    public TokenPairResponse refreshToken(RefreshTokenRequestDTO dto) {

        String oldToken = dto.refreshToken();
        String userIdString = jwtService.extractUserId(oldToken);

        UUID userId = UUID.fromString(userIdString);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new TokenPairResponse(newAccessToken, newRefreshToken);

    }

}
