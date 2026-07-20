package dev.cameloasa.todoapi.integration.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.service.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class AuthApiNegIntegrRegisterTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @MockBean private EmailServiceImpl emailService;

  // ---------------------------------------------------------
  // TEST: register email already exists
  // ---------------------------------------------------------

  @SuppressWarnings("null")
  @Test
  void testRegisterEmailAlreadyExists() throws Exception {

    seedUserAndPerson(); // email = test@example.com

    String json =
        """
    {
        "firstName": "Test",
        "lastName": "Person",
        "email": "test@example.com",
        "username": "newuser",
        "password": "Password123!"
    }
    """;

    mockMvc
        .perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isConflict());
  }

  // ---------------------------------------------------------
  // TEST: register username already exists
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegisterUsernameAlreadyExists() throws Exception {

    seedUserAndPerson(); // username = test

    String json =
        """
    {
        "firstName": "Camelia",
        "lastName": "Test",
        "email": "new@example.com",
        "username": "test",
        "password": "Password123!"
    }
    """;

    mockMvc
        .perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isConflict());
  }

  // ---------------------------------------------------------
  // TEST: register invalid email
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegisterInvalidEmail() throws Exception {

    doNothing().when(emailService).sendRegistrationEmail(anyString());

    String json =
        """
    {
        "firstName": "Camelia",
        "lastName": "Test",
        "email": "invalid-email",
        "username": "newuser",
        "password": "Password123!"
    }
    """;

    mockMvc
        .perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
  }

  // ---------------------------------------------------------
  // TEST: register missing fields
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegisterMissingFields() throws Exception {

    String json =
        """
    {
        "firstName": "",
        "lastName": "",
        "email": "",
        "username": "",
        "password": ""
    }
    """;

    mockMvc
        .perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
  }
}
