package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.SessionResponseDTO;

public interface AuthService {

  SessionResponseDTO register(RegisterDTOForm dto);

  SessionResponseDTO login(LoginDTOForm dto);

  void logout(String sessionToken);

  SessionResponseDTO me(String sessionToken);
}
