package com.microservice.authservice.service;

import com.microservice.authservice.dto.RegisterRequestDTO;
import com.microservice.authservice.model.User;
import com.microservice.authservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

@Service
@Service
public class AuthService {

    @Autowired
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

}
