package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;

public interface RoleConverter {

    RoleDTOView toRoleDTO(Role entity);
    Role toRoleEntity(RoleDTOView dto);
}
