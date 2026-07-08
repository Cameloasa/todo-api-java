package dev.cameloasa.todoapi.integration.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.service.EmailService;
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
public class UserApiIntegrationTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @MockBean private EmailService emailService;

  // ---------------------------------------------------------
  // Positive tests
  // ---------------------------------------------------------
  // ---------------------------------------------------------
  // Register User Test
  // ---------------------------------------------------------

  @SuppressWarnings("null")
  @Test
  void testRegisterUser() throws Exception {

    Long userRoleId = roleRepository.findByName("USER").get().getId();

    String json =
        """
            {
                "email": "test@example.com",
                "username": "testuser",
                "password": "Password123#",
                "roleIds": [%d]
            }
            """
            .formatted(userRoleId);

    mockMvc
        .perform(post("/users").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.roles[0].name").value("USER"));
  }

  // ---------------------------------------------------------
  // Get user by email test
  // ---------------------------------------------------------
  @Test
  void testGetUserByEmail() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(get("/users").param("email", "test@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.username").value("test"));
  }

  // ---------------------------------------------------------
  // Get user by username test
  // ---------------------------------------------------------
  @Test
  void testGetUserByUsername() throws Exception {
    var user = createUser("test@example.com");

    mockMvc
        .perform(get("/users/username").param("username", user.getUsername()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.username").value(user.getUsername()));
  }

  // ---------------------------------------------------------
  // Get all users test
  // ---------------------------------------------------------
  @Test
  void testGetAllUsers() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(get("/users/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  // ---------------------------------------------------------
  // Disable user test
  // ---------------------------------------------------------
  @Test
  void testDisableUser() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(put("/users/disable").param("email", "test@example.com"))
        .andExpect(status().isNoContent());

    User user = userRepository.findByEmail("test@example.com").get();
    assertTrue(user.isExpired());
  }

  // ---------------------------------------------------------
  // Enable user test
  // ---------------------------------------------------------
  @Test
  void testEnableUser() throws Exception {
    var user = createUser("test@example.com");
    user.setExpired(true);
    userRepository.save(user);

    mockMvc
        .perform(put("/users/enable").param("email", "test@example.com"))
        .andExpect(status().isNoContent());

    User updated = userRepository.findByEmail("test@example.com").get();
    assertFalse(updated.isExpired());
  }

  // ---------------------------------------------------------
  // Update user test
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testUpdateUser() throws Exception {
    createUser("test@example.com");

    Long adminRoleId = roleRepository.findByName("ADMIN").get().getId();

    String json =
        """
                {
                    "email": "test@example.com",
                    "username": "newuser",
                    "password": "Newpass123!",
                    "roleIds": [%d],
                    "expired": false
                }
                """
            .formatted(adminRoleId);

    mockMvc
        .perform(
            patch("/users")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("newuser"))
        .andExpect(jsonPath("$.roles[0].name").value("ADMIN"));
  }

  // ---------------------------------------------------------
  // Delete user test
  // ---------------------------------------------------------
  @Test
  void testDeleteUser() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(delete("/users").param("email", "test@example.com"))
        .andExpect(status().isNoContent());

    assertFalse(userRepository.findByEmail("test@example.com").isPresent());
  }

  // ---------------------------------------------------------
  // Negative tests
  // ---------------------------------------------------------

  // ---------------------------------------------------------
  // Invalid password
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegisterUser_InvalidPassword() throws Exception {

    Long userRoleId = roleRepository.findByName("USER").get().getId();

    String json =
        """
                {
                    "email": "test@example.com",
                    "username": "testuser",
                    "password": "weakpass",
                    "roleIds": [%d]
                }
                """
            .formatted(userRoleId);

    mockMvc
        .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "password: Password must contain at least one uppercase letter, one lowercase letter, one number, one special character."));
  }

  // ---------------------------------------------------------
  // email missing
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegisterUser_EmailMissing() throws Exception {

    Long userRoleId = roleRepository.findByName("USER").get().getId();

    String json =
        """
                {
                    "username": "testuser",
                    "password": "Password123#",
                    "roleIds": [%d]
                }
                """
            .formatted(userRoleId);

    mockMvc
        .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("email: Email is required."));
  }

  // ---------------------------------------------------------
  // invalid role
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testRegisterUser_RoleInvalid() throws Exception {

    String json =
        """
                {
                    "email": "test@example.com",
                    "username": "testuser",
                    "password": "Password123#",
                    "roleIds": [999]
                }
                """;

    mockMvc
        .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Role is not valid"));
  }

  // ---------------------------------------------------------
  // update user not found
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testUpdateUser_UserNotFound() throws Exception {

    Long adminRoleId = roleRepository.findByName("ADMIN").get().getId();

    String json =
        """
                {
                    "email": "ghost@example.com",
                    "username": "newuser",
                    "password": "Password123#",
                    "roleIds": [%d],
                    "expired": false
                }
                """
            .formatted(adminRoleId);

    mockMvc
        .perform(
            patch("/users")
                .param("email", "ghost@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Email not found."));
  }

  // ---------------------------------------------------------
  // update invalid role
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void testUpdateUser_RoleInvalid() throws Exception {

    createUser("test@example.com");

    String json =
        """
                {
                    "email": "test@example.com",
                    "username": "newuser",
                    "password": "Password123#",
                    "roleIds": [999],
                    "expired": false
                }
                """;

    mockMvc
        .perform(
            patch("/users")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Role is not valid"));
  }
}
