package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;

public interface UserService {

  // Register user (email, password, roles)
  UserDTOView register(UserDTOForm dtoForm);

  // Find user by email
  UserDTOView getByEmail(String email);

  // Disable user (expired = true)
  void disableEmail(String email);

  // Enable user (expired = false)
  void enableEmail(String email);
}
