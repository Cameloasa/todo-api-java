package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.domanin.entity.User;

public class SessionFixture {

  public static SessionEntity sampleSession(User user) {
    SessionEntity session = new SessionEntity();
    session.setToken("session-token-123");
    session.setUserEmail(user.getEmail());
    session.setCreatedAt(System.currentTimeMillis());
    session.setExpiresAt(System.currentTimeMillis() + 3600_000); // +1 hour
    return session;
  }

  public static SessionEntity validSession(User user) {
        SessionEntity session = new SessionEntity();
        session.setToken("valid-token-123");
        session.setUserEmail(user.getEmail());
        session.setCreatedAt(System.currentTimeMillis());
        session.setExpiresAt(System.currentTimeMillis() + 3600_000); // +1 hour
        return session;
    }

    public static SessionEntity expiredSession(User user) {
        SessionEntity session = new SessionEntity();
        session.setToken("expired-token-123");
        session.setUserEmail(user.getEmail());
        session.setCreatedAt(System.currentTimeMillis() - 7200_000); // 2 hours ago
        session.setExpiresAt(System.currentTimeMillis() - 3600_000); // expired 1 hour ago
        return session;
    }

    public static SessionEntity customTokenSession(User user, String token) {
        SessionEntity session = new SessionEntity();
        session.setToken(token);
        session.setUserEmail(user.getEmail());
        session.setCreatedAt(System.currentTimeMillis());
        session.setExpiresAt(System.currentTimeMillis() + 3600_000);
        return session;
    }

    public static SessionEntity sessionWithoutEmail() {
        SessionEntity session = new SessionEntity();
        session.setToken("no-email-token");
        session.setUserEmail(null);
        session.setCreatedAt(System.currentTimeMillis());
        session.setExpiresAt(System.currentTimeMillis() + 3600_000);
        return session;
    }
}
