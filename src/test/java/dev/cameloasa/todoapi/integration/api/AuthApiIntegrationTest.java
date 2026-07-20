package dev.cameloasa.todoapi.integration.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class AuthApiIntegrationTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @MockBean private EmailServiceImpl emailService;

  // ---------------------------------------------------------
  // TEST 1: register
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegister() throws Exception {

    doNothing().when(emailService).sendRegistrationEmail(anyString());

    String json =
        """
        {
            "firstName": "Test",
            "lastName": "Person",
            "email": "test@example.com",
            "username": "test",
            "password": "Password123!"
        }
    """;

    mockMvc
        .perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.user.username").value("test"))
        .andExpect(jsonPath("$.user.email").value("test@example.com"))
        .andExpect(jsonPath("$.person.firstName").value("Test"))
        .andExpect(jsonPath("$.person.lastName").value("Person"))
        .andExpect(jsonPath("$.success").value(true));
  }

  // ---------------------------------------------------------
  // TEST 2: login
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testLogin() throws Exception {

    // 1. Register
    String registerJson =
        """
        {
            "firstName": "Test",
            "lastName": "Person",
            "email": "test@example.com",
            "username": "test",
            "password": "Password123!"
        }
    """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerJson))
        .andDo(print())
        .andExpect(status().isCreated());

    // 2. Login
    String loginJson =
        """
        {
            "email": "test@example.com",
            "password": "Password123!"
        }
    """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
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

    // 1. Register
    String registerJson =
        """
        {
            "firstName": "Test",
            "lastName": "Person",
            "email": "test@example.com",
            "username": "test",
            "password": "Password123!"
        }
    """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerJson))
        .andExpect(status().isCreated());

    // 2. Login
    String loginJson =
        """
        {
            "email": "test@example.com",
            "password": "Password123!"
        }
    """;

    MvcResult result =
        mockMvc
            .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
            .andExpect(status().isOk())
            .andReturn();

    String token = result.getResponse().getCookie("session_token").getValue();

    // 3. Me
    mockMvc
        .perform(get("/auth/me").header("X-Session-Token", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("test"))
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

    // 1. Register
    String registerJson =
        """
        {
            "firstName": "Test",
            "lastName": "Person",
            "email": "test@example.com",
            "username": "test",
            "password": "Password123!"
        }
    """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerJson))
        .andExpect(status().isCreated());

    // 2. Login
    String loginJson =
        """
        {
            "email": "test@example.com",
            "password": "Password123!"
        }
    """;

    MvcResult result =
        mockMvc
            .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
            .andExpect(status().isOk())
            .andReturn();

    String token = result.getResponse().getCookie("session_token").getValue();

    // 3. Logout
    mockMvc
        .perform(post("/auth/logout").header("X-Session-Token", token))
        .andExpect(status().isOk());
  }
}
