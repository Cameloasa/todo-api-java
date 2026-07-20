package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthConverterImpl implements AuthConverter {

  private final RoleRepository roleRepository;

  @Override
  public UserDTOForm toUserDTOForm(RegisterDTOForm dto) {

    Role userRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new RuntimeException("Default role USER not found"));

    UserDTOForm form = new UserDTOForm();
    form.setEmail(dto.getEmail());
    form.setUsername(dto.getUsername());
    form.setPassword(dto.getPassword());
    form.setRoleIds(List.of(userRole.getId()));
    return form;
  }

  @Override
  public PersonDTOForm toPersonDTOForm(RegisterDTOForm dto) {
    PersonDTOForm form = new PersonDTOForm();
    form.setFirstName(dto.getFirstName());
    form.setLastName(dto.getLastName());
    form.setUserEmail(dto.getEmail());
    return form;
  }

  @Override
  public LoginDTOForm toLoginDTOForm(LoginDTOForm dto) {
    
    return dto;
  }
}
