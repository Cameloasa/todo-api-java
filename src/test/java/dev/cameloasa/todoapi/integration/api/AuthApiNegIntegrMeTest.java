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
public class AuthApiNegIntegrMeTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  // ---------------------------------------------------------
  // TEST: me missing token
  // ---------------------------------------------------------
  @Test
  void testMeMissingToken() throws Exception {
    mockMvc.perform(get("/auth/me")).andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // TEST: me invalid token
  // ---------------------------------------------------------
  @Test
  void testMeInvalidToken() throws Exception {
    mockMvc
        .perform(get("/auth/me").header("X-Session-Token", "invalid-token"))
        .andExpect(status().isUnauthorized());
  }

  // ---------------------------------------------------------
  // TEST: me expired token
  // ---------------------------------------------------------
  @Test
  void testMeExpiredToken() throws Exception {

    createUser("test@example.com");

    LocalDateTime expiredAt = LocalDateTime.now().minusHours(1);
    long expiredMillis = expiredAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    SessionEntity session = new SessionEntity();
    session.setToken("expired-token");
    session.setUserEmail("test@example.com");
    session.setExpiresAt(expiredMillis); // expirat
    sessionRepository.save(session);

    mockMvc
        .perform(get("/auth/me").header("X-Session-Token", "expired-token"))
        .andExpect(status().isUnauthorized());
  }
}
