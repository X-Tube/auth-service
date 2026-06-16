package com.microservice.authservice.repository;

import com.microservice.authservice.model.User;
import com.microservice.authservice.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserVerificationRepository extends JpaRepository<UserVerification, UUID> {

    Optional<UserVerification> findByUser(User user);

    void deleteByUser(User user);
}
