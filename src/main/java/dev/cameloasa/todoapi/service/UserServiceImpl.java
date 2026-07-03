package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.UserConverter;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserConverter userConverter;

  public UserServiceImpl(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      UserConverter userConverter) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.userConverter = userConverter;
  }

  @Override
  @Transactional
  public UserDTOView register(UserDTOForm dtoForm) {

    if (dtoForm == null) {
      throw new IllegalArgumentException("UserDTOForm cannot be null");
    }

    if (userRepository.existsByEmail(dtoForm.getEmail())) {
      throw new DataDuplicateException("Email already exists");
    }

    // Validate roles
    Set<Role> roles =
        dtoForm.getRoleIds().stream()
            .map(
                roleId ->
                    roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new DataNotFoundException("Role is not valid")))
            .collect(Collectors.toSet());

    // Convert DTO → Entity
    User user = userConverter.toUserEntity(dtoForm);

    // Encode password
    user.setPassword(passwordEncoder.encode(dtoForm.getPassword()));

    // Set roles
    user.setRoles(roles);

    // Save
    User savedUser = userRepository.save(user);

    // Convert Entity → DTOView
    return userConverter.toUserDTOView(savedUser);
  }

  @Override
  public UserDTOView getByEmail(String email) {
    User user =
        userRepository
            .findById(email)
            .orElseThrow(() -> new DataNotFoundException("Email not found."));

    return userConverter.toUserDTOView(user);
  }

  @Override
  @Transactional
  public void disableEmail(String email) {
    validateEmailExists(email);
    userRepository.updateExpiredByEmail(email, true);
  }

  @Override
  @Transactional
  public void enableEmail(String email) {
    validateEmailExists(email);
    userRepository.updateExpiredByEmail(email, false);
  }

  @Override
  public UserDTOView getByUsername(String username) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new DataNotFoundException("Username not found."));

    return userConverter.toUserDTOView(user);
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public java.util.List<UserDTOView> getAll() {
    return userRepository.findAll().stream()
        .map(userConverter::toUserDTOView)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserDTOView update(String email, UserDTOForm dtoForm) {
    validateEmailExists(email);

    User existingUser = userRepository.findById(email).orElseThrow();

    // Update fields
    existingUser.setUsername(dtoForm.getUsername());
    existingUser.setEmail(dtoForm.getEmail());
    existingUser.setPassword(passwordEncoder.encode(dtoForm.getPassword()));
    existingUser.setExpired(dtoForm.isExpired());

    // Update roles
    Set<Role> roles =
        dtoForm.getRoleIds().stream()
            .map(
                roleId ->
                    roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new DataNotFoundException("Role is not valid")))
            .collect(Collectors.toSet());
    existingUser.setRoles(roles);

    // Save updated user
    User updatedUser = userRepository.save(existingUser);

    return userConverter.toUserDTOView(updatedUser);
  }

  @Override
  @Transactional
  public void delete(String email) {
    validateEmailExists(email);
    userRepository.deleteById(email);
  }

  private void validateEmailExists(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new DataNotFoundException("Email not found.");
    }
  }


}
