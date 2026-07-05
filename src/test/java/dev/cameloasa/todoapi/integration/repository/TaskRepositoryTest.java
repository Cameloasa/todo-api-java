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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TaskRepositoryTest {

    @Autowired private TaskRepository taskRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private UserRepository userRepository;

    private Person person;
    private Task task1, task2, task3, task4;

    @BeforeEach
    void setUp() {
        // Clean DB for isolation
        taskRepository.deleteAll();
        personRepository.deleteAll();
        userRepository.deleteAll();

        // Create User
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setExpired(false);
        userRepository.save(user);

        // Create Person
        person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setUser(user);
        personRepository.save(person);

        // Task 1 — assigned, not done, deadline tomorrow
        task1 = new Task();
        task1.setTitle("Buy milk");
        task1.setDescription("Go to store");
        task1.setDeadline(LocalDate.now().plusDays(1));
        task1.setDone(false);
        task1.setPerson(person);
        taskRepository.save(task1);

        // Task 2 — assigned, done, deadline today
        task2 = new Task();
        task2.setTitle("Clean room");
        task2.setDescription("Vacuum and dust");
        task2.setDeadline(LocalDate.now());
        task2.setDone(true);
        task2.setPerson(person);
        taskRepository.save(task2);

        // Task 3 — assigned, overdue
        task3 = new Task();
        task3.setTitle("Pay bills");
        task3.setDescription("Electricity and water");
        task3.setDeadline(LocalDate.now().minusDays(3));
        task3.setDone(false);
        task3.setPerson(person); // IMPORTANT: owner required
        taskRepository.save(task3);

        // Task 4 — assigned, not done, deadline in 10 days
        task4 = new Task();
        task4.setTitle("Prepare presentation");
        task4.setDescription("Slides for meeting");
        task4.setDeadline(LocalDate.now().plusDays(10));
        task4.setDone(false);
        task4.setPerson(person);
        taskRepository.save(task4);
    }

    // ---------------------------------------------------------
    // TEST 1: findByTitleContaining
    // ---------------------------------------------------------
    @Test
    void testFindByTitleContaining() {
        List<Task> results = taskRepository.findByTitleContaining("Clean");
        assertEquals(1, results.size());
        assertEquals("Clean room", results.get(0).getTitle());
    }

    // ---------------------------------------------------------
    // TEST 2: findByPersonId
    // ---------------------------------------------------------
    @Test
    void testFindByPersonId() {
        List<Task> results = taskRepository.findByPersonId(person.getId());
        assertEquals(4, results.size()); // all tasks have owner
    }

    // ---------------------------------------------------------
    // TEST 3: findByDone
    // ---------------------------------------------------------
    @Test
    void testFindByDone() {
        List<Task> doneTasks = taskRepository.findByDone(true);
        List<Task> notDoneTasks = taskRepository.findByDone(false);

        assertEquals(1, doneTasks.size());
        assertEquals(3, notDoneTasks.size());
    }

    // ---------------------------------------------------------
    // TEST 4: findByDeadlineBetween
    // ---------------------------------------------------------
    @Test
    void testFindByDeadlineBetween() {
        List<Task> results =
            taskRepository.findByDeadlineBetween(LocalDate.now(), LocalDate.now().plusDays(5));

        assertEquals(2, results.size()); // task1 + task2
    }

    // ---------------------------------------------------------
    // TEST 5: findByDoneFalse
    // ---------------------------------------------------------
    @Test
    void testFindByDoneFalse() {
        List<Task> results = taskRepository.findByDoneFalse();
        assertEquals(3, results.size());
    }

    // ---------------------------------------------------------
    // TEST 6: findUnfinishedAndOverdueTasks
    // ---------------------------------------------------------
    @Test
    void testFindUnfinishedAndOverdueTasks() {
        List<Task> results = taskRepository.findUnfinishedAndOverdueTasks(LocalDate.now());
        assertEquals(1, results.size());
        assertEquals("Pay bills", results.get(0).getTitle());
    }

    // ---------------------------------------------------------
    // TEST 7: findByPersonIdAndDone
    // ---------------------------------------------------------
    @Test
    void testFindByPersonIdAndDone() {
        List<Task> results = taskRepository.findByPersonIdAndDone(person.getId(), false);
        assertEquals(3, results.size());
    }

    // ---------------------------------------------------------
    // TEST 8: findByDeadlineAndDone
    // ---------------------------------------------------------
    @Test
    void testFindByDeadlineAndDone() {
        List<Task> results = taskRepository.findByDeadlineAndDone(LocalDate.now(), true);
        assertEquals(1, results.size());
        assertEquals("Clean room", results.get(0).getTitle());
    }

    // ---------------------------------------------------------
    // TEST 9: findByDeadline
    // ---------------------------------------------------------
    @Test
    void testFindByDeadline() {
        List<Task> results = taskRepository.findByDeadline(LocalDate.now());
        assertEquals(1, results.size());
    }

    // ---------------------------------------------------------
    // TEST 10: findByDeadlineAfter
    // ---------------------------------------------------------
    @Test
    void testFindByDeadlineAfter() {
        List<Task> results = taskRepository.findByDeadlineAfter(LocalDate.now());
        assertEquals(2, results.size()); // task1 + task4
    }

    // ---------------------------------------------------------
    // TEST 11: findOverdueTasks
    // ---------------------------------------------------------
    @Test
    void testFindOverdueTasks() {
        List<Task> results = taskRepository.findOverdueTasks();
        assertEquals(1, results.size());
        assertEquals("Pay bills", results.get(0).getTitle());
    }

    // ---------------------------------------------------------
    // TEST 12: findByDescriptionContaining
    // ---------------------------------------------------------
    @Test
    void testFindByDescriptionContaining() {
        List<Task> results = taskRepository.findByDescriptionContaining("store");
        assertEquals(1, results.size());
        assertEquals("Buy milk", results.get(0).getTitle());
    }

    // ---------------------------------------------------------
    // TEST 13: findByPersonIdAndTitleContaining
    // ---------------------------------------------------------
    @Test
    void testFindByPersonIdAndTitleContaining() {
        List<Task> results = taskRepository.findByPersonIdAndTitleContaining(person.getId(), "Prepare");
        assertEquals(1, results.size());
        assertEquals("Prepare presentation", results.get(0).getTitle());
    }
}
