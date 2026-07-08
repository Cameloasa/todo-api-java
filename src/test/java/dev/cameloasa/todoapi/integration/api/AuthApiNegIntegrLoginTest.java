package dev.cameloasa.todoapi.integration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class AuthApiNegIntegrLoginTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  // ---------------------------------------------------------
  // TEST: login with wrong password
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLoginWrongPassword() throws Exception {

    createUser("test@example.com");
    String json =
        """
                {
                    "username": "testuser",
                    "email": "test@example.com",
                    "password": "wrongpass"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  // ---------------------------------------------------------
  // TEST: login wrong username
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLoginWrongUsername() throws Exception {

    createUser("test@example.com");
    String json =
        """
                {
                    "username": "wronguser",
                    "password": "Password123!"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  // ---------------------------------------------------------
  // TEST: login wrong email
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLoginWrongEmail() throws Exception {

    createUser("test@example.com");
    String json =
        """
                {
                    "username": "test",
                    "email": "wrong@example.com",
                    "password": "Password123!"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  // ---------------------------------------------------------
  // TEST: login missing email
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLoginMissingEmail() throws Exception {

    createUser("test@example.com");

    String json =
        """
                {

                    "password": "Password123!"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // TEST: login missing username
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLoginMissingUsername() throws Exception {

    createUser("test@example.com");
    String json =
        """
                {
                    "email": "test@example.com",

                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // TEST: login missing password
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLoginMissingPassword() throws Exception {

    createUser("test@example.com");
    String json =
        """
                {

                    "email": "test@example.com"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
