package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.RoleConverter;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.repository.RoleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleConverter roleConverter;

  public RoleServiceImpl(RoleRepository roleRepository, RoleConverter roleConverter) {
    this.roleRepository = roleRepository;
    this.roleConverter = roleConverter;
  }

  @Override
  public List<RoleDTOView> getAll() {
    return roleRepository.findAll().stream().map(roleConverter::toRoleDTOView).toList();
  }

  @Override
public RoleDTOView create(RoleDTOForm dtoForm) {
    Role role = new Role(dtoForm.getName());
    Role saved = roleRepository.save(role);
    return roleConverter.toRoleDTOView(saved); // <-- corect
}


}
