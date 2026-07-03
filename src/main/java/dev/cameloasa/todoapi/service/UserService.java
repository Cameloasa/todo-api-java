package dev.cameloasa.todoapi.service;

import java.util.List;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;

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

    // Update user (email, username, roles, expired)
    UserDTOView update(String email, UserDTOForm dtoForm);

    // Delete user
    void delete(String email);

    // Disable user (expired = true)
    void disableEmail(String email);

    // Enable user (expired = false)
    void enableEmail(String email);
}
