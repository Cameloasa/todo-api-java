package dev.cameloasa.todoapi.fixtures;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;

public class AuthFixture {

  public static RegisterDTOForm sampleRegisterForm() {
    RegisterDTOForm dto = new RegisterDTOForm();
    dto.setFirstName("Camelia");
    dto.setLastName("Test");
    dto.setEmail("test@example.com");
    dto.setUsername("testuser");
    dto.setPassword("password123");
    return dto;
  }

  public static LoginDTOForm sampleLoginForm() {
    LoginDTOForm dto = new LoginDTOForm();
    dto.setUsername("testuser");
    dto.setPassword("password123");
    return dto;
  }

  public static User sampleUser() {
    return UserFixture.sampleUser();
  }

  public static Person samplePerson(User user) {
    return PersonFixture.samplePerson(user);
  }

  public static Role sampleRole() {
    return RoleFixture.sampleRole();
  }

  public static SessionEntity sampleSession(User user) {
    return SessionFixture.sampleSession(user);
  }
}
