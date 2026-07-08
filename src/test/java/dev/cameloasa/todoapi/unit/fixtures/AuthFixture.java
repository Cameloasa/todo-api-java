package dev.cameloasa.todoapi.unit.fixtures;

import java.util.List;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.domanin.dto.EmailDTO;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.domanin.entity.User;


public class AuthFixture {

  // -------------------------
    // Register / Login DTOs
    // -------------------------
    public static RegisterDTOForm sampleRegisterForm() {
        RegisterDTOForm dto = new RegisterDTOForm();
        dto.setFirstName("Test");
        dto.setLastName("Person");
        dto.setEmail("test@example.com");
        dto.setUsername("test");
        dto.setPassword("Password123!");
        return dto;
    }

    public static LoginDTOForm sampleLoginForm() {
        LoginDTOForm dto = new LoginDTOForm();
        dto.setUsername("test");
        dto.setPassword("Password123!");
        return dto;
    }

    // -------------------------
    // User entities
    // -------------------------
    public static User sampleUser() {
        return UserFixture.sampleUser();
    }

    public static User sampleUserWithEncodedPassword() {
        return UserFixture.sampleUserWithEncodedPassword();
    }

    public static User sampleUserWithCustomEmail(String email) {
        return UserFixture.sampleUserWithCustomEmail(email);
    }

    // -------------------------
    // Person entities
    // -------------------------
    public static Person samplePerson(User user) {
        return PersonFixture.samplePerson(user);
    }

    public static Person samplePersonWithTasks(User user, List<Task> tasks) {
        return PersonFixture.samplePersonWithTasks(user, tasks);
    }

    // -------------------------
    // Role entities
    // -------------------------
    public static Role sampleRole() {
        return RoleFixture.sampleRole();
    }

    // -------------------------
    // Session
    // -------------------------
    public static SessionEntity sampleSession(User user) {
        return SessionFixture.sampleSession(user);
    }

    // -------------------------
    // Email DTOs
    // -------------------------
    public static EmailDTO sampleRegistrationEmail(String to) {
        return EmailFixture.sampleRegistrationEmail(to);
    }

    public static EmailDTO sampleEmailDTO() {
        return EmailFixture.sampleEmailDTO();
    }
}
