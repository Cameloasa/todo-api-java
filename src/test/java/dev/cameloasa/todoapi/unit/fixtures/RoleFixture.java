package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;

public class RoleFixture {

  public static Role sampleRole() {
    Role role = new Role();
    role.setId(1L);
    role.setName("USER");
    return role;
  }

  public static RoleDTOView sampleRoleDTOView() {
        return RoleDTOView.builder()
                .id(1L)
                .name("USER")
                .build();
    }

  public static RoleDTOForm sampleRoleForm() {
        RoleDTOForm dto = new RoleDTOForm();
        dto.setId(1L);
        dto.setName("USER");
        return dto;
    }
}
