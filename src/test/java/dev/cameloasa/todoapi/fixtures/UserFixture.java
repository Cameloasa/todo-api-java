package dev.cameloasa.todoapi.fixtures;

import dev.cameloasa.todoapi.domanin.entity.User;

public class UserFixture {

  public static User sampleUser() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setUsername("testuser");
    user.setPassword("password123");
    user.setExpired(false);
    return user;
  }
}
