package dev.cameloasa.todoapi.fixtures;

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
}
