package com.microservice.authservice.service;

import com.microservice.authservice.dto.LoginRequestDTO;
import com.microservice.authservice.dto.LoginResponseDTO;
import com.microservice.authservice.dto.RegisterRequestDTO;
import com.microservice.authservice.model.User;
import com.microservice.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequestDTO dto) {

        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(user);

        return "New user successfully registered!";
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(token);
    }

}
