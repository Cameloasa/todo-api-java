package dev.cameloasa.todoapi.integration.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.UserRepository;

import dev.cameloasa.todoapi.auth.session.SessionRepository;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;


@SpringBootTest
@ActiveProfiles("test") 
@TestPropertySource("classpath:application-test.properties")
public abstract class IntegrationTestBase {

    @Autowired protected UserRepository userRepository;
    @Autowired protected PersonRepository personRepository;
    @Autowired protected SessionRepository sessionRepository;
    @Autowired protected RoleRepository roleRepository;

    @BeforeEach
    void cleanDatabase() {
        sessionRepository.deleteAll();
        personRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("GUEST"));
    }

    protected User seedUserAndPerson() {
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
}

