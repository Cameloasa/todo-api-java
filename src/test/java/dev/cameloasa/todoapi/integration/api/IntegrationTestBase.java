package dev.cameloasa.todoapi.integration.api;

import dev.cameloasa.todoapi.auth.session.SessionRepository;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public abstract class IntegrationTestBase {

  @Autowired protected UserRepository userRepository;
  @Autowired protected PersonRepository personRepository;
  @Autowired protected SessionRepository sessionRepository;
  @Autowired protected RoleRepository roleRepository;
  @Autowired protected TaskRepository taskRepository;
  @Autowired protected PasswordEncoder passwordEncoder;

  @BeforeEach
  void cleanDatabase() {
    sessionRepository.deleteAll();
    taskRepository.deleteAll();
    personRepository.deleteAll();
    userRepository.deleteAll();
    roleRepository.deleteAll();

    roleRepository.save(new Role("ADMIN"));
    roleRepository.save(new Role("USER"));
    roleRepository.save(new Role("GUEST"));
  }

  // -------------------------
  // USER HELPERS
  // -------------------------
  protected User createUser(String email) {
    User u = new User();
    u.setEmail(email);
    u.setUsername(email.split("@")[0]);
    u.setPassword(passwordEncoder.encode("Password123!"));
    u.setExpired(false);
    return userRepository.save(u);
  }

  // -------------------------
  // PERSON HELPERS
  // -------------------------
  protected Person createPerson(User user, String first, String last) {
    Person p = new Person();
    p.setFirstName(first);
    p.setLastName(last);
    p.setUser(user);
    return personRepository.save(p);
  }

  // -------------------------
  // TASK HELPERS
  // -------------------------
  protected Task createTask(Person person, String title, String description) {
    Task t = new Task();
    t.setTitle(title);
    t.setDescription(description);
    t.setDone(false);
    t.setPerson(person);
    return taskRepository.save(t);
  }

  // -------------------------
  // SEED DEFAULT USER + PERSON
  // -------------------------
  protected User seedUserAndPerson() {
    User user = createUser("test@example.com");
    createPerson(user, "Test", "Person");
    return user;
  }
}
