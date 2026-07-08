package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.entity.Role;

public class RoleFixture {

  public static Role sampleRole() {
    Role role = new Role();
    role.setName("USER");
    return role;
  }
}
