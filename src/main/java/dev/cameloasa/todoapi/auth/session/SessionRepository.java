package dev.cameloasa.todoapi.auth.session;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {

    void deleteByUserEmail(String email);

    boolean existsByToken(String token);

    SessionEntity findByToken(String token);
}

