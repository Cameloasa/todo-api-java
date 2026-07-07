package dev.cameloasa.todoapi.auth.service;

import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.auth.session.SessionRepository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;

  public String createSession(String userEmail) {
    SessionEntity session = new SessionEntity();
    session.setToken(UUID.randomUUID().toString());
    session.setUserEmail(userEmail);
    session.setCreatedAt(System.currentTimeMillis());
    session.setExpiresAt(System.currentTimeMillis() + 1000L * 60 * 60 * 24); // 24h

    sessionRepository.save(session);
    return session.getToken();
  }

  public boolean isValid(String token) {
    Optional<SessionEntity> sessionOpt = sessionRepository.findByToken(token);
    if (sessionOpt.isEmpty()) return false;
    SessionEntity session = sessionOpt.get();
    return session.getExpiresAt() > System.currentTimeMillis();
  }

  public String getUserEmail(String token) {
    Optional<SessionEntity> sessionOpt = sessionRepository.findByToken(token);
    if (sessionOpt.isEmpty()) return null;
    return sessionOpt.get().getUserEmail();
  }

  public void deleteSession(@NonNull String token) {
    sessionRepository.deleteById(token);
  }

  public Optional<SessionEntity> getSession(String token) {
    return sessionRepository.findByToken(token);
}
}
