package dev.cameloasa.todoapi.auth.session;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {

  void deleteByUserEmail(String email);

  boolean existsByToken(String token);

  Optional<SessionEntity> findByToken(String token);
}
