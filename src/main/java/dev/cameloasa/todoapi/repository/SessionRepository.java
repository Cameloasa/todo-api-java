package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.SessionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {

  void deleteByUserEmail(String email);

  boolean existsByToken(String token);

  Optional<SessionEntity> findByToken(String token);
}
