package dev.cameloasa.todoapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cameloasa.todoapi.domanin.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByEmail(String email);
}

