package dev.cameloasa.todoapi.integration.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.fixtures.SessionFixture;
import dev.cameloasa.todoapi.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class AuthApiIntegrationTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;
 
  @MockBean private EmailService emailService;


  // ---------------------------------------------------------
  // TEST 1: register
  // ---------------------------------------------------------
  @Test
  void testRegister() throws Exception {

    when(emailService.sendRegistrationEmail(anyString())).thenReturn(HttpStatus.OK);

    String json =
        """
        {
            "firstName": "Test",
            "lastName": "Person",
            "email": "test@example.com",
            "username": "testuser",
            "password": "Password123!"
        }
        """;

    mockMvc
        .perform(post("/auth/register").contentType("application/json").content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.user.username").value("testuser"))
        .andExpect(jsonPath("$.user.email").value("test@example.com"))
        .andExpect(jsonPath("$.person.firstName").value("Test"))
        .andExpect(jsonPath("$.person.lastName").value("Person"))
        .andExpect(jsonPath("$.success").value(true));
  }

  // ---------------------------------------------------------
  // TEST 2: login
  // ---------------------------------------------------------
  @Test
  void testLogin() throws Exception {
  
    String json =
        """
        {
            "username": "testuser",
            "email": "test@example.com",
            "password": "Password123!"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(cookie().exists("session_token"));
  }

  // ---------------------------------------------------------
  // TEST 3: me (requires session)
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testMe() throws Exception {
    User user = createUser("test@example.com");
    

    SessionEntity session = SessionFixture.sampleSession(user);
    sessionRepository.save(session);

    mockMvc
        .perform(get("/auth/me").header("X-Session-Token", session.getToken()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("testuser"))
        .andExpect(jsonPath("$.user.email").value("test@example.com"))
        .andExpect(jsonPath("$.person.firstName").value("Test"))
        .andExpect(jsonPath("$.person.lastName").value("Person"))
        .andExpect(jsonPath("$.success").value(true));
  }

  // ---------------------------------------------------------
  // TEST 4: logout
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLogout() throws Exception {
    User user = createUser("test@example.com");
    

    SessionEntity session = SessionFixture.sampleSession(user);
    sessionRepository.save(session);

    mockMvc
        .perform(post("/auth/logout").header("X-Session-Token", session.getToken()))
        .andExpect(status().isOk());
  }
}
