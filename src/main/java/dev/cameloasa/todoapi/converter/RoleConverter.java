package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;

public interface RoleConverter {

    RoleDTOView toRoleDTOView(Role entity);

    Role toRoleEntity(RoleDTOForm dto);
}
