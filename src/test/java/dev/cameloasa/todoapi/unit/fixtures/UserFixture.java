package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.entity.User;

public class UserFixture {

  public static User sampleUser() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setUsername("test");
    user.setPassword("Password123!");
    user.setExpired(false);
    return user;
  }
}
