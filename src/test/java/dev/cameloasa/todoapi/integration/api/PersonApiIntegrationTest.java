package dev.cameloasa.todoapi.integration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class PersonApiIntegrationTest extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  // -------------------------
  // Positive tests
  // -------------------------

    @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testFindPersonById() throws Exception {
    var user = createUser("test@example.com");
    var person = createPerson(user, "Ana", "Pop");

    mockMvc
        .perform(get("/auth/persons/" + person.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Ana"))
        .andExpect(jsonPath("$.lastName").value("Pop"));
  }

  @SuppressWarnings("null")
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testUpdatePerson() throws Exception {
    var user = createUser("test@example.com");
    var person = createPerson(user, "Maria", "Ionescu");

    String updateJson =
        """
                {
                    "id": %d,
                    "firstName": "Mariana",
                    "lastName": "Ionescu",
                    "userEmail": "test@example.com"
                }
                """
            .formatted(person.getId());

    mockMvc
        .perform(patch("/auth/persons").contentType(MediaType.APPLICATION_JSON).content(updateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Mariana"));
  }

  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testDeletePerson() throws Exception {
    var user = createUser("test@example.com");
    var person = createPerson(user, "Alex", "Popescu");

    mockMvc.perform(delete("/auth/persons/" + person.getId())).andExpect(status().isNoContent());
  }

  // -------------------------
  // Negative tests
  // -------------------------

  @SuppressWarnings("null")
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testCreatePerson_UserNotFound() throws Exception {
    // do not create user -> 404

    String json =
        """
            {
                "firstName": "Ghost",
                "lastName": "User",
                "userEmail": "missing@example.com"
            }
            """;

    mockMvc
        .perform(post("/auth/persons").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound());
  }

  @SuppressWarnings("null")
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testCreatePerson_DuplicatePerson() throws Exception {
    var user = createUser("test@example.com");
    createPerson(user, "Ana", "Pop"); // deja există

    String json =
        """
            {
                "firstName": "Duplicate",
                "lastName": "Person",
                "userEmail": "test@example.com"
            }
            """;

    mockMvc
        .perform(post("/auth/persons").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  void testFindPersonById_NotFound() throws Exception {
    mockMvc.perform(get("/persons/999")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  void testDeletePerson_NotFound() throws Exception {
    mockMvc.perform(delete("/auth/persons/999")).andExpect(status().isNotFound());
  }

  @SuppressWarnings("null")
  @WithMockUser(roles = {"ADMIN", "SUPERADMIN"})
  @Test
  void testUpdatePerson_NotFound() throws Exception {
    String json =
        """
            {
                "id": 999,
                "firstName": "Nobody",
                "lastName": "Missing",
                "userEmail": "test@example.com"
            }
            """;

    mockMvc
        .perform(patch("/auth/persons").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound());
  }
}
