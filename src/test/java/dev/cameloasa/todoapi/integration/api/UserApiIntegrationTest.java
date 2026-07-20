package dev.cameloasa.todoapi.integration.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.domanin.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class UserApiIntegrationTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  // ---------------------------------------------------------
  // Positive tests
  // ---------------------------------------------------------
  // ---------------------------------------------------------
  // Get user by email test
  // ---------------------------------------------------------
  @WithMockUser(roles = "USER")
  @Test
  void testGetUserByEmail() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(get("/auth/users/email").param("email", "test@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.username").value("test"));
  }

  // ---------------------------------------------------------
  // Get user by username test
  // ---------------------------------------------------------
  @WithMockUser(roles = "USER")
  @Test
  void testGetUserByUsername() throws Exception {
    var user = createUser("test@example.com");

    mockMvc
        .perform(get("/auth/users/username").param("username", user.getUsername()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.username").value(user.getUsername()));
  }

  // ---------------------------------------------------------
  // Get all users test
  // ---------------------------------------------------------
  @WithMockUser(roles = "USER")
  @Test
  void testGetAllUsers() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(get("/auth/users/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  // ---------------------------------------------------------
  // Disable user test
  // ---------------------------------------------------------
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testDisableUser() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(put("/auth/users/disable").param("email", "test@example.com"))
        .andExpect(status().isNoContent());

    User user = userRepository.findByEmail("test@example.com").get();
    assertTrue(user.isExpired());
  }

  // ---------------------------------------------------------
  // Enable user test
  // ---------------------------------------------------------
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testEnableUser() throws Exception {
    var user = createUser("test@example.com");
    user.setExpired(true);
    userRepository.save(user);

    mockMvc
        .perform(put("/auth/users/enable").param("email", "test@example.com"))
        .andExpect(status().isNoContent());

    User updated = userRepository.findByEmail("test@example.com").get();
    assertFalse(updated.isExpired());
  }

  // ---------------------------------------------------------
  // Update reset password by admin test
  // ---------------------------------------------------------
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testResetPasswordByAdmin() throws Exception {
    // Arrange: creează userul în DB
    createUser("test@example.com");

    String json =
        """
                {
                    "password": "Newpass123!"
                }
                """;

    // Act + Assert
    mockMvc
        .perform(
            patch("/auth/users/password")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  // ---------------------------------------------------------
  // Delete user test
  // ---------------------------------------------------------
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testDeleteUser() throws Exception {
    createUser("test@example.com");

    mockMvc
        .perform(delete("/auth/users").param("email", "test@example.com"))
        .andExpect(status().isNoContent());

    assertFalse(userRepository.findByEmail("test@example.com").isPresent());
  }

  // ---------------------------------------------------------
  // Negative tests
  // ---------------------------------------------------------

}
