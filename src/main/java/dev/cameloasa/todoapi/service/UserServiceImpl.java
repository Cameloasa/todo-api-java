package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.UserConverter;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.PasswordResetToken;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.repository.PasswordResetTokenRepository;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PersonRepository personRepository;
  private final RoleRepository roleRepository;
  
  
  private final UserConverter userConverter;

  private final PasswordEncoder passwordEncoder;
  private final EmailServiceImpl emailService;

  @Override
  @Transactional
  public UserDTOView register(UserDTOForm dtoForm) {

    if (dtoForm == null) {
      throw new IllegalArgumentException("UserDTOForm cannot be null");
    }

    if (userRepository.existsByEmail(dtoForm.getEmail())) {
      throw new DataDuplicateException("Email already exists");
    }

    if (userRepository.existsByUsername(dtoForm.getUsername())) {
      throw new DataDuplicateException("Username already exists");
    }

    // Validate roles
    @SuppressWarnings("null")
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
    @SuppressWarnings("null")
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
  public List<UserDTOView> getAll() {
    return userRepository.findAll().stream()
        .map(userConverter::toUserDTOView)
        .collect(Collectors.toList());
  }

  @Override
@Transactional
public UserDTOView resetPassword(String email, String newPassword) {
    validateEmailExists(email);

    User user = userRepository.findById(email).orElseThrow();

    String encoded = passwordEncoder.encode(newPassword);
    userRepository.updatePasswordByEmail(email, encoded);

    // token = null → admin reset
    emailService.sendPasswordResetEmail(email, null);

    return userConverter.toUserDTOView(user);
}


  @Transactional
  public UserDTOView updateRoles(String email, List<Long> roleIds) {

    User user =
        userRepository
            .findByEmailWithRoles(email)
            .orElseThrow(() -> new DataNotFoundException("User not found"));

    Set<Role> roles =
        roleIds.stream()
            .map(
                id ->
                    roleRepository
                        .findById(id)
                        .orElseThrow(() -> new DataNotFoundException("Role not found")))
            .collect(Collectors.toSet());

    user.setRoles(roles);

    return userConverter.toUserDTOView(userRepository.save(user));
  }

  @SuppressWarnings("null")
  @Override
  @Transactional
  public void delete(String email) {
    validateEmailExists(email);
    // delete person first
    personRepository.deleteByUserEmail(email);

    // delete user
    userRepository.deleteById(email);
  }

  private void validateEmailExists(String email) {
    if (!userRepository.existsByEmail(email)) {
      throw new DataNotFoundException("Email not found.");
    }
  }
}
