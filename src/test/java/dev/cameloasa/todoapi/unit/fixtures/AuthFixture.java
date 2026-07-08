package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;


public class AuthFixture {

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
