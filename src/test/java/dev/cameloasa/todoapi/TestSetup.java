package dev.cameloasa.todoapi;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestSetup {

  @Autowired private UserRepository userRepository;

  @Autowired private PersonRepository personRepository;

  @Autowired private TaskRepository taskRepository;

  @BeforeAll
  static void globalSetup() {}

  @BeforeAll
  void seedDatabase() {

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

    Task task = new Task();
    task.setTitle("Sample Task");
    task.setDescription("This is a sample task for testing.");
    task.setDeadline(java.time.LocalDate.now().plusDays(7));
    task.setDone(false);
    task.setPerson(person);

    taskRepository.save(task);
  }
}
