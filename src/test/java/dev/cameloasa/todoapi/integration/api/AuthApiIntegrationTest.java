package dev.cameloasa.todoapi.integration.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.auth.session.SessionRepository;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.fixtures.SessionFixture;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
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
public class AuthApiIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private SessionRepository sessionRepository;

    @MockBean private EmailService emailService;

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
        user.setPassword("password123");
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
    // TEST 1: register
    // ---------------------------------------------------------
    @Test
    void testRegister() throws Exception {

        when(emailService.sendRegistrationEmail(anyString())).thenReturn(HttpStatus.OK);

        String json =
            """
            {
                "firstName": "Camelia",
                "lastName": "Test",
                "email": "new@example.com",
                "username": "newuser",
                "password": "pass123"
            }
            """;

        mockMvc
            .perform(post("/auth/register").contentType("application/json").content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.user.username").value("newuser"))
            .andExpect(jsonPath("$.user.email").value("new@example.com"))
            .andExpect(jsonPath("$.person.firstName").value("Camelia"))
            .andExpect(jsonPath("$.person.lastName").value("Test"))
            .andExpect(jsonPath("$.success").value(true));
    }

    // ---------------------------------------------------------
    // TEST 2: login
    // ---------------------------------------------------------
    @Test
    void testLogin() throws Exception {
        seedUserAndPerson(); // user trebuie să existe

        String json =
            """
            {
                "username": "testuser",
                "email": "test@example.com",
                "password": "password123"
            }
            """;

        mockMvc
            .perform(post("/auth/login")
            .contentType("application/json").content(json))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("session_token"));
    }

    // ---------------------------------------------------------
    // TEST 3: me (requires session)
    // ---------------------------------------------------------
    @SuppressWarnings("null")
    @Test
    void testMe() throws Exception {
        User user = seedUserAndPerson();

        SessionEntity session = SessionFixture.sampleSession(user);
        sessionRepository.save(session);

        mockMvc
            .perform(get("/auth/me").header("X-Session-Token", session.getToken()))
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
        User user = seedUserAndPerson();

        SessionEntity session = SessionFixture.sampleSession(user);
        sessionRepository.save(session);

        mockMvc
            .perform(post("/auth/logout").header("X-Session-Token", session.getToken()))
            .andExpect(status().isOk());
    }
}
