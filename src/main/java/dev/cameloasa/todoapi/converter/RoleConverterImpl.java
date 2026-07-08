package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;
import org.springframework.stereotype.Component;
@Component
public class RoleConverterImpl implements RoleConverter {

  @Override
  public RoleDTOView toRoleDTOView(Role entity) {
    if (entity == null) return null;

    return RoleDTOView.builder().id(entity.getId()).name(entity.getName()).build();
  }

  @Override
  public Role toRoleEntity(RoleDTOForm dto) {
    if (dto == null) return null;

    return Role.builder().id(dto.getId()).name(dto.getName()).build();
  }
}

