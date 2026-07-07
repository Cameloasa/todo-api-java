package dev.cameloasa.todoapi.integration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.auth.session.SessionRepository;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthApiNegIntegrLoginTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PersonRepository personRepository;

  @Autowired private SessionRepository sessionRepository;

  // ---------------------------------------------------------
  // TEST: login with wrong password
  // ---------------------------------------------------------
  @Test
  void testLoginWrongPassword() throws Exception {
    seedUser();

    String json =
        """
            {
                "username": "testuser",
                "email": "test@example.com",
                "password": "wrongpass"
            }
            """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andExpect(status().isUnauthorized());
  }

  // ---------------------------------------------------------
  // TEST: login wrong username
  // ---------------------------------------------------------
  @Test
  void testLoginWrongUsername() throws Exception {
    seedUser();

    String json =
        """
        {
            "username": "wronguser",
            "password": "password123"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andExpect(status().isNotFound());
  }

  // ---------------------------------------------------------
  // TEST: login wrong email
  // ---------------------------------------------------------
  @Test
  void testLoginWrongEmail() throws Exception {
    seedUser();

    String json =
        """
        {
            "username": "testuser",
            "email": "wrong@example.com",
            "password": "password123"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andExpect(status().isNotFound());
  }

  // ---------------------------------------------------------
  // TEST: login missing email
  // ---------------------------------------------------------
  @Test
  void testLoginMissingEmail() throws Exception {
    seedUser();

    String json = """
        {

            "password": "password123"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // TEST: login missing username
  // ---------------------------------------------------------
  @Test
  void testLoginMissingUsername() throws Exception {
    seedUser();

    String json = """
        {
            "email": "test@example.com",

        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // TEST: login missing password
  // ---------------------------------------------------------
  @Test
  void testLoginMissingPassword() throws Exception {
    seedUser();

    String json = """
        {

            "email": "test@example.com"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType("application/json").content(json))
        .andExpect(status().isBadRequest());
  }

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
}
