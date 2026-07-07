package dev.cameloasa.todoapi.integration.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.service.EmailService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthApiNegIntegrRegisterTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;

    @MockBean private EmailService emailService;

  // ---------------------------------------------------------
  // TEST: register email already exists
  // ---------------------------------------------------------

    @Test
    void testRegisterEmailAlreadyExists() throws Exception {
        // seed user
        User user = new User();
        user.setEmail("existing@example.com");
        user.setUsername("existinguser");
        user.setPassword("pass123");
        userRepository.save(user);

        String json =
            """
            {
                "firstName": "Camelia",
                "lastName": "Test",
                "email": "existing@example.com",
                "username": "newuser",
                "password": "pass123"
            }
            """;

        mockMvc
            .perform(post("/auth/register").contentType("application/json").content(json))
            .andExpect(status().isConflict());
    }

    // ---------------------------------------------------------
    // TEST: register username already exists
    // ---------------------------------------------------------
    @Test
    void testRegisterUsernameAlreadyExists() throws Exception {
        User user = new User();
        user.setEmail("unique@example.com");
        user.setUsername("existinguser");
        user.setPassword("pass123");
        userRepository.save(user);

        String json =
            """
            {
                "firstName": "Camelia",
                "lastName": "Test",
                "email": "new@example.com",
                "username": "existinguser",
                "password": "pass123"
            }
            """;

        mockMvc
            .perform(post("/auth/register").contentType("application/json").content(json))
            .andExpect(status().isConflict());
    }

    // ---------------------------------------------------------
    // TEST: register invalid email
    // ---------------------------------------------------------
    @Test
    void testRegisterInvalidEmail() throws Exception {

        when(emailService.sendRegistrationEmail(anyString())).thenReturn(HttpStatus.OK);
        String json =
            """
            {
                "firstName": "Camelia",
                "lastName": "Test",
                "email": "invalid-email",
                "username": "newuser",
                "password": "pass123"
            }
            """;

        mockMvc
            .perform(post("/auth/register")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------
    // TEST: register missing fields
    // ---------------------------------------------------------
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
            .perform(post("/auth/register").contentType("application/json").content(json))
            .andExpect(status().isBadRequest());
    }
}
