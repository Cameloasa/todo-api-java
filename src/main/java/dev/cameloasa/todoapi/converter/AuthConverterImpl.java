package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AuthConverterImpl implements AuthConverter {

  private static final Long DEFAULT_ROLE_ID = 1L; // rolul USER

  @Override
  public UserDTOForm toUserDTOForm(RegisterDTOForm dto) {
    UserDTOForm form = new UserDTOForm();
    form.setEmail(dto.getEmail());
    form.setUsername(dto.getUsername());
    form.setPassword(dto.getPassword());
    form.setRoleIds(List.of(DEFAULT_ROLE_ID));
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
    // nu e nevoie de conversie, dar păstrăm metoda pentru consistență
    return dto;
  }
}
