package dev.cameloasa.todoapi.integration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.domanin.entity.SessionEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class AuthApiNegIntegrLogoutTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  // ---------------------------------------------------------
  // Logout: missing tocken
  // ---------------------------------------------------------
  @Test
  void testLogoutMissingToken() throws Exception {
    mockMvc.perform(post("/auth/logout")).andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // Logout: invalid tocken
  // ---------------------------------------------------------
  @Test
  void testLogoutInvalidToken() throws Exception {
    mockMvc
        .perform(post("/auth/logout").header("X-Session-Token", "invalid-token"))
        .andExpect(status().isUnauthorized());
  }

  // ---------------------------------------------------------
  // Logout: expired tocken
  // ---------------------------------------------------------
  @Test
  void testLogoutExpiredToken() throws Exception {

    createUser("test@example.com");

    LocalDateTime expiredAt = LocalDateTime.now().minusHours(1);
    long expiredMillis = expiredAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    SessionEntity session = new SessionEntity();
    session.setToken("expired-token");
    session.setUserEmail("test@example.com");
    session.setExpiresAt(expiredMillis);
    sessionRepository.save(session);

    mockMvc
        .perform(post("/auth/logout").header("X-Session-Token", "expired-token"))
        .andExpect(status().isUnauthorized());
  }
}
