package com.microservice.authservice.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendVerificationEmail(String toEmail, String code) {
        System.out.println("\n=================================================");
        System.out.println("SENDING EMAIL TO: " + toEmail);
        System.out.println("SUBJECT: Xtube - Verify Your Account");
        System.out.println("BODY: Welcome to Xtube! Your verification code is: " + code);
        System.out.println("This code will expire in 15 minutes.");
        System.out.println("=================================================\n");
    }
}
