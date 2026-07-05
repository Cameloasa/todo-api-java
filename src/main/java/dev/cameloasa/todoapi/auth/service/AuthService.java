package dev.cameloasa.todoapi.auth.service;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOView;
import dev.cameloasa.todoapi.auth.dto.SessionResponseDTO;

public interface AuthService {

  RegisterDTOView register(RegisterDTOForm dto);

  SessionResponseDTO login(LoginDTOForm dto);

  void logout(String sessionToken);

  SessionResponseDTO me(String sessionToken);
}
