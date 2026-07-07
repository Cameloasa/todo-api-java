package dev.cameloasa.todoapi.integration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.auth.session.SessionRepository;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthApiNegIntegrMeTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PersonRepository personRepository;

  @Autowired private SessionRepository sessionRepository;

  // Helper: seed user
  // ---------------------------------------------------------

  // ---------------------------------------------------------
  // Helper: seed user
  // ---------------------------------------------------------
  private User seedUser() {
    sessionRepository.deleteAll();
    personRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setEmail("test@example.com");
    user.setUsername("testuser");
    user.setPassword("password123");
    userRepository.save(user);

    Person person = new Person();
    person.setFirstName("Test");
    person.setLastName("User");
    person.setUser(user);
    personRepository.save(person);

    return user;
  }

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
    seedUser();
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
