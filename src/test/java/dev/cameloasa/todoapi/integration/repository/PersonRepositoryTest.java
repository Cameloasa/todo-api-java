package dev.cameloasa.todoapi.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonRepositoryTest {

    @Autowired private PersonRepository personRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
        personRepository.deleteAll();
        userRepository.deleteAll();
    }

    // ---------------------------------------------------------
    // TEST 1: findByUserEmail
    // ---------------------------------------------------------
    @Test
    void testFindByUserEmail() {
        User u = createUser("test@example.com");

        Person p = new Person();
        p.setFirstName("John");
        p.setLastName("Smith");
        p.setUser(u);
        personRepository.save(p);

        Optional<Person> found = personRepository.findByUserEmail("test@example.com");

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Smith", found.get().getLastName());
    }

    // ---------------------------------------------------------
    // TEST 2: findByFirstNameContainingIgnoreCase
    // ---------------------------------------------------------
    @Test
    void testFindByFirstNameContainingIgnoreCase() {
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Smith");
        p1.setUser(createUser("john@example.com"));
        personRepository.save(p1);

        Person p2 = new Person();
        p2.setFirstName("JOHNNY");
        p2.setLastName("Test");
        p2.setUser(createUser("johnny@example.com"));
        personRepository.save(p2);

        List<Person> results = personRepository.findByFirstNameContainingIgnoreCase("john");

        assertEquals(2, results.size());
    }

    // ---------------------------------------------------------
    // TEST 3: findByLastNameContainingIgnoreCase
    // ---------------------------------------------------------
    @Test
    void testFindByLastNameContainingIgnoreCase() {
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Smith");
        p1.setUser(createUser("john@example.com"));
        personRepository.save(p1);

        Person p2 = new Person();
        p2.setFirstName("Jane");
        p2.setLastName("SMITHSON");
        p2.setUser(createUser("jane@example.com"));
        personRepository.save(p2);

        List<Person> results = personRepository.findByLastNameContainingIgnoreCase("smith");

        assertEquals(2, results.size());
    }

    // ---------------------------------------------------------
    // TEST 4: findIdlePeople
    // ---------------------------------------------------------
    @Test
    void testFindIdlePeople() {
        // Person with NO tasks (idle)
        Person idle = new Person();
        idle.setFirstName("Idle");
        idle.setLastName("Person");
        idle.setUser(createUser("idle@example.com"));
        personRepository.save(idle);

        // Person WITH tasks (busy)
        Person busy = new Person();
        busy.setFirstName("Busy");
        busy.setLastName("Person");
        busy.setUser(createUser("busy@example.com"));
        personRepository.save(busy);

        Task task = new Task();
        task.setTitle("Sample Task");
        task.setDescription("Testing task");
        task.setDeadline(LocalDate.now().plusDays(3));
        task.setDone(false);
        task.setPerson(busy);
        taskRepository.save(task);

        List<Person> results = personRepository.findIdlePeople();

        assertEquals(1, results.size());
        assertEquals("Idle", results.get(0).getFirstName());
    }

    // ---------------------------------------------------------
    // Helper method
    // ---------------------------------------------------------
    private User createUser(String email) {
        User u = new User();
        u.setEmail(email);
        u.setUsername(email.split("@")[0]);
        u.setPassword("pass");
        u.setExpired(false);
        return userRepository.save(u);
    }
}