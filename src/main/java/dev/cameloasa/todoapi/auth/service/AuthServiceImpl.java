package dev.cameloasa.todoapi.auth.service;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOView;
import dev.cameloasa.todoapi.auth.dto.SessionResponseDTO;
import dev.cameloasa.todoapi.auth.session.SessionEntity;
import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.UserConverter;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.exception.EmailServiceFailedException;
import dev.cameloasa.todoapi.exception.InvalidCredentialsException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.service.EmailService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PersonRepository personRepository;
  private final SessionService sessionService;
  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserConverter userConverter;
  private final PersonConverter personConverter;
  private final EmailService emailService;

  // ---------------------------------------------------------
  // Register
  // ---------------------------------------------------------
  @Override
  public RegisterDTOView register(RegisterDTOForm dto) {

    // verify email
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new DataDuplicateException("Email already registered");
    }

    // verify username
    if (userRepository.existsByUsername(dto.getUsername())) {
      throw new DataDuplicateException("Username already taken");
    }

    // create default role USER if not exists
    Role userRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new DataNotFoundException("Default role USER not found"));

    // create user
    User user = new User();
    user.setEmail(dto.getEmail());
    user.setUsername(dto.getUsername());
    user.setPassword(dto.getPassword());
    user.setExpired(false);
    user.setRoles(Set.of(userRole));

    userRepository.save(user);

    // create person
    Person person = new Person();
    person.setFirstName(dto.getFirstName());
    person.setLastName(dto.getLastName());
    person.setUser(user);

    personRepository.save(person);

    // 🔥 integration with email service
    try {
      emailService.sendRegistrationEmail(user.getEmail());
    } catch (EmailServiceFailedException ex) {
      // poți decide ce faci:
      // fie ignori și continui
      // fie arunci excepția mai departe
      throw ex;
    }

    // convert to DTO view
    RegisterDTOView response = new RegisterDTOView();
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(personConverter.toPersonDTOView(person));
    response.setSuccess(true);

    return response;
  }

  // ---------------------------------------------------------
  // Login
  // ---------------------------------------------------------
  @Override
  public SessionResponseDTO login(LoginDTOForm dto) {

    User user = null;

    // login with email
    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      user =
          userRepository
              .findByEmail(dto.getEmail())
              .orElseThrow(() -> new DataNotFoundException("Invalid email"));
    }

    // login with username
    else if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
      user =
          userRepository
              .findByUsername(dto.getUsername())
              .orElseThrow(() -> new DataNotFoundException("Invalid username"));
    } else {
      throw new IllegalArgumentException("Email or username must be provided");
    }

    // verify password

    if (dto.getPassword() == null || dto.getPassword().isBlank()) {
      throw new IllegalArgumentException("Password is required");
    }
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid password");
    }

    // create session
    String token = sessionService.createSession(user.getEmail());

    // search Person (person has a reference to user, so we can search by user email)
    Person person =
        personRepository
            .findByUserEmail(user.getEmail())
            .orElseThrow(() -> new DataNotFoundException("Person not found for user"));

    // build response
    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(token);
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(personConverter.toPersonDTOView(person));
    response.setSuccess(true);

    return response;
  }

  // ---------------------------------------------------------
  // Logout:
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Override
  public void logout(String sessionToken) {

    if (!sessionService.isValid(sessionToken)) {
      throw new InvalidCredentialsException("Session not found");
    }

    sessionService.deleteSession(sessionToken);
  }

  // ---------------------------------------------------------
  // Me: get current user and person by session token
  // ---------------------------------------------------------
  @Override
  public SessionResponseDTO me(String sessionToken) {

    // 1. Validate token presence
    if (sessionToken == null || sessionToken.isBlank()) {
      throw new IllegalArgumentException("Session token is required");
    }

    // 2. Fetch session object
    SessionEntity session =
        sessionService
            .getSession(sessionToken)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid session token"));

    // 3. Validate session (expired, revoked, etc.)
    if (!sessionService.isValid(sessionToken)) {
      throw new InvalidCredentialsException("Session expired");
    }

    // 4. Extract email from session
    String email = session.getUserEmail();

    // 5. Fetch user
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new DataNotFoundException("User not found"));

    // 6. Fetch person
    Person person =
        personRepository
            .findByUserEmail(email)
            .orElseThrow(() -> new DataNotFoundException("Person not found"));

    // 7. Build response
    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(sessionToken);
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(personConverter.toPersonDTOView(person));
    response.setSuccess(true);

    return response;
  }
}
