package dev.cameloasa.todoapi.integration.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.auth.session.SessionRepository;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @MockBean
    private EmailService emailService;

    // ---------------------------------------------------------
    // Helper: seed user
    // ---------------------------------------------------------
    private User seedUserAndPerson() {
        // delete all existing users, persons, and sessions to ensure a clean state
        sessionRepository.deleteAll();
        personRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("Password123!");
        user.setExpired(false);
        userRepository.save(user);

        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Person");
        person.setUser(user);
        personRepository.save(person);

        return user;
    }

    // ---------------------------------------------------------
    // Register User Test
    // ---------------------------------------------------------
    @Test
    void testRegisterUser() throws Exception {

        String json = """
                {
                    "email": "test@example.com",
                    "username": "testuser",
                    "password": "Password123!",
                    "roleIds": [2],
                    "expired": false
                }
                """;

        mockMvc
                .perform(post("/users").contentType("application/json").content(json))
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
        seedUserAndPerson();

        mockMvc
                .perform(get("/users").param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    // ---------------------------------------------------------
    // Get user by username test
    // ---------------------------------------------------------
    @Test
    void testGetUserByUsername() throws Exception {
        seedUserAndPerson();

        mockMvc
                .perform(get("/users/username").param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    // ---------------------------------------------------------
    // Get all users test
    // ---------------------------------------------------------
    @Test
    void testGetAllUsers() throws Exception {
        seedUserAndPerson();

        mockMvc
                .perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ---------------------------------------------------------
    // Get disabled users test
    // ---------------------------------------------------------
    @Test
    void testDisableUser() throws Exception {
        seedUserAndPerson();

        mockMvc
                .perform(put("/users/disable").param("email", "test@example.com"))
                .andExpect(status().isNoContent());

        User user = userRepository.findByEmail("test@example.com").get();
        assertTrue(user.isExpired());
    }

    // ---------------------------------------------------------
    // Get enabled users test
    // ---------------------------------------------------------
    @Test
    void testEnableUser() throws Exception {
        seedUserAndPerson(); // user is created with expired = false

        mockMvc
                .perform(put("/users/enable").param("email", "test@example.com"))
                .andExpect(status().isNoContent());

        User user = userRepository.findByEmail("test@example.com").get();
        assertFalse(user.isExpired()); // user should remain enabled
    }

    // ---------------------------------------------------------
    // Get update users test
    // ---------------------------------------------------------
    @Test
    void testUpdateUser() throws Exception {
        seedUserAndPerson();

        String json = """
                {
                    "email": "test@example.com",
                    "username": "newuser",
                    "password": "Newpass123!",
                    "roleIds": [1],
                    "expired": false
                }
                """;

        mockMvc
                .perform(
                        patch("/users")
                                .param("email", "test@example.com")
                                .contentType("application/json")
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.roles[0].name").value("ADMIN"));
    }

    // ---------------------------------------------------------
    // Get delete users test
    // ---------------------------------------------------------
    @Test
    void testDeleteUser() throws Exception {
        seedUserAndPerson();

        mockMvc
                .perform(delete("/users").param("email", "test@example.com"))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findByEmail("test@example.com").isPresent());
    }
}
