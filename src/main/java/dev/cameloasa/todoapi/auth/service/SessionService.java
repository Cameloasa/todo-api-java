package dev.cameloasa.todoapi.auth.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.auth.session.SessionRepository;
import lombok.RequiredArgsConstructor;

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
        SessionEntity session = sessionRepository.findByToken(token);
        if (session == null) return false;
        return session.getExpiresAt() > System.currentTimeMillis();
    }

    public String getUserEmail(String token) {
        SessionEntity session = sessionRepository.findByToken(token);
        if (session == null) return null;
        return session.getUserEmail();
    }

    public void deleteSession(String token) {
        sessionRepository.deleteById(token);
    }
}

