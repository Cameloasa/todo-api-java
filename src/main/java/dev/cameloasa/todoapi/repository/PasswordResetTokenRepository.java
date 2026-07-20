package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.PasswordResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

  Optional<PasswordResetToken> findByToken(String token);

  void deleteByEmail(String email);
}
