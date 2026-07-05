package dev.cameloasa.todoapi.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.UserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setExpired(false);
        userRepository.save(user);
    }

    // ---------------------------------------------------------
    // TEST 1: existsByEmail
    // ---------------------------------------------------------
    @Test
    void testExistsByEmail() {
        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("unknown@example.com"));
    }

    // ---------------------------------------------------------
    // TEST 2: findByEmail
    // ---------------------------------------------------------
    @Test
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    // ---------------------------------------------------------
    // TEST 3: existsByUsername
    // ---------------------------------------------------------
    @Test
    void testExistsByUsername() {
        assertTrue(userRepository.existsByUsername("testuser"));
        assertFalse(userRepository.existsByUsername("unknown"));
    }

    // ---------------------------------------------------------
    // TEST 4: findByUsername
    // ---------------------------------------------------------
    @Test
    void testFindByUsername() {
        Optional<User> found = userRepository.findByUsername("testuser");

        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    // ---------------------------------------------------------
    // TEST 5: findByEmailOrUsername
    // ---------------------------------------------------------
    @Test
    void testFindByEmailOrUsername() {
        Optional<User> found1 = userRepository.findByEmailOrUsername("test@example.com");
        Optional<User> found2 = userRepository.findByEmailOrUsername("testuser");

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
    }

    // ---------------------------------------------------------
    // TEST 6: existsByEmailAndExpired
    // ---------------------------------------------------------
    @Test
    void testExistsByEmailAndExpired() {
        assertTrue(userRepository.existsByEmailAndExpired("test@example.com", false));
        assertFalse(userRepository.existsByEmailAndExpired("test@example.com", true));
    }

    // ---------------------------------------------------------
    // TEST 7: findByEmailAndExpired
    // ---------------------------------------------------------
    @Test
    void testFindByEmailAndExpired() {
        Optional<User> found = userRepository.findByEmailAndExpired("test@example.com", false);

        assertTrue(found.isPresent());
    }

    // ---------------------------------------------------------
    // TEST 8: updateExpiredByEmail
    // ---------------------------------------------------------
    @Test
    @Transactional
    void testUpdateExpiredByEmail() {
        userRepository.updateExpiredByEmail("test@example.com", true);

        entityManager.flush();
        entityManager.clear();

        Optional<User> updated = userRepository.findByEmail("test@example.com");

        assertTrue(updated.isPresent());
        assertTrue(updated.get().isExpired());
    }

    // ---------------------------------------------------------
    // TEST 9: updatePasswordByEmail
    // ---------------------------------------------------------
    @Test
    @Transactional
    void testUpdatePasswordByEmail() {
        userRepository.updatePasswordByEmail("test@example.com", "newpass");

        entityManager.flush();
        entityManager.clear();

        Optional<User> updated = userRepository.findByEmail("test@example.com");

        assertTrue(updated.isPresent());
        assertEquals("newpass", updated.get().getPassword());
    }
}
