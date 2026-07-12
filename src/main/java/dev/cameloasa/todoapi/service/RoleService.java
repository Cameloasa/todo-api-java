package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import java.util.List;

public interface RoleService {

  RoleDTOView create(RoleDTOForm dtoForm);
  List<RoleDTOView> getAll();
}
