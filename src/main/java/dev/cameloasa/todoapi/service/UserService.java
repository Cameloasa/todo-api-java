package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import java.util.List;

public interface UserService {

  /// Register user
  UserDTOView register(UserDTOForm dtoForm);

  // Get user by email
  UserDTOView getByEmail(String email);

  // Get user by username
  UserDTOView getByUsername(String username);

  // Check if username exists
  boolean existsByUsername(String username);

  // Get all users
  List<UserDTOView> getAll();

  // Reset password (reset password, expired)
  UserDTOView resetPassword(String email, String newPassword);

  // Update user ( roles)
  UserDTOView updateRoles(String email, List<Long> roleIds);

  // Delete user
  void delete(String email);

  // Disable user (expired = true)
  void disableEmail(String email);

  // Enable user (expired = false)
  void enableEmail(String email);
}
