package com.microservice.authservice.service;

import com.microservice.authservice.dto.*;
import com.microservice.authservice.model.User;
import com.microservice.authservice.model.UserVerification;
import com.microservice.authservice.repository.UserRepository;

import com.microservice.authservice.repository.UserVerificationRepository;
import com.microservice.authservice.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserVerificationRepository userVerificationRepository;

    @Transactional
    public String register(RegisterRequestDTO dto) {

        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        User savedUser = userRepository.save(user);

        String code = String.format("%06d", new Random().nextInt(999999));

        UserVerification verification = new UserVerification();
        verification.setCode(code);
        verification.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        verification.setUser(savedUser);
        userVerificationRepository.save(verification);

        emailService.sendVerificationEmail(user.getEmail(), code);

        return  "Registration successful. Please check your email for the verification code.";
    }

    @Transactional
    public TokenPairResponse verifyUser(VerifyRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already verified");
        }

        UserVerification verification = userVerificationRepository.findByUser(user)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification code not found"));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }

        if (!verification.getCode().equals(dto.code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code");
        }

        user.setEnabled(true);
        userRepository.save(user);

        userVerificationRepository.delete(verification);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenPairResponse(accessToken, refreshToken);
    }

    @Transactional
    public String resendVerificationCode(ResendCodeRequestDTO dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already verified");
        }

        String newCode = String.format("%06d", new java.util.Random().nextInt(999999));

        UserVerification verification = userVerificationRepository.findByUser(user)
                        .orElse(new UserVerification());

        verification.setUser(user);
        verification.setCode(newCode);
        verification.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        userVerificationRepository.save(verification);

        emailService.sendVerificationEmail(user.getEmail(), newCode);

        return "A new verification code has been sent.";
    }

    public TokenPairResponse login(LoginRequestDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

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