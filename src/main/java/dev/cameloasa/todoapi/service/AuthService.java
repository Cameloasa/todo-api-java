package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.SessionResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

  SessionResponseDTO register(RegisterDTOForm dto);

  SessionResponseDTO login(LoginDTOForm dto, HttpServletResponse response);

  void logout(String sessionToken);

  SessionResponseDTO me(String sessionToken);

  void requestPasswordReset(String email);

  void confirmPasswordReset(String token, String newPassword);
}
