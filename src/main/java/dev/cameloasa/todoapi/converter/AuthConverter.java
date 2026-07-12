package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;

public interface AuthConverter {

  UserDTOForm toUserDTOForm(RegisterDTOForm dto);

  PersonDTOForm toPersonDTOForm(RegisterDTOForm dto);

  LoginDTOForm toLoginDTOForm(LoginDTOForm dto);
}
