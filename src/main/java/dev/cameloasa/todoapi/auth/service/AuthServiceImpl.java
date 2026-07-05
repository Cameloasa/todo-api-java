package dev.cameloasa.todoapi.auth.service;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOView;
import dev.cameloasa.todoapi.auth.dto.SessionResponseDTO;
import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.UserConverter;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Role;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.exception.EmailServiceFailedException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.RoleRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import dev.cameloasa.todoapi.service.EmailService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PersonRepository personRepository;
  private final SessionService sessionService;
  private final RoleRepository roleRepository;

  private final UserConverter userConverter;
  private final PersonConverter personConverter;
  private final EmailService emailService;

  @Override
  public RegisterDTOView register(RegisterDTOForm dto) {

    // verificăm email
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new DataDuplicateException("Email already registered");
    }

    // verificăm username
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

    // 🔥 AICI INTEGRAM EMAIL SERVICE
    try {
      emailService.sendRegistrationEmail(user.getEmail());
    } catch (EmailServiceFailedException ex) {
      // poți decide ce faci:
      // fie ignori și continui
      // fie arunci excepția mai departe
      throw ex;
    }

    // convertim la DTO view
    RegisterDTOView response = new RegisterDTOView();
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(personConverter.toPersonDTOView(person));
    response.setSuccess(true);

    return response;
  }

  @Override
  public SessionResponseDTO login(LoginDTOForm dto) {

    User user = null;

    // login cu email
    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      user =
          userRepository
              .findByEmail(dto.getEmail())
              .orElseThrow(() -> new RuntimeException("Invalid email"));
    }

    // login cu username
    else if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
      user =
          userRepository
              .findByUsername(dto.getUsername())
              .orElseThrow(() -> new RuntimeException("Invalid username"));
    } else {
      throw new RuntimeException("Email or username must be provided");
    }

    // verificăm parola
    if (!user.getPassword().equals(dto.getPassword())) {
      throw new RuntimeException("Invalid password");
    }

    // creăm sesiune
    String token = sessionService.createSession(user.getEmail());

    // căutăm person (acum Person are user, nu userEmail)
    Person person =
        personRepository
            .findByUserEmail(user.getEmail())
            .orElseThrow(() -> new RuntimeException("Person not found for user"));

    // construim răspuns
    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(token);
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(personConverter.toPersonDTOView(person));
    response.setSuccess(true);

    return response;
  }

  @Override
  public void logout(String sessionToken) {

    if (!sessionService.isValid(sessionToken)) {
      throw new RuntimeException("Session not found");
    }

    sessionService.deleteSession(sessionToken);
  }

  @Override
  public SessionResponseDTO me(String sessionToken) {

    if (!sessionService.isValid(sessionToken)) {
      throw new RuntimeException("Session expired or invalid");
    }

    String email = sessionService.getUserEmail(sessionToken);

    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new DataNotFoundException("User not found"));
    Person person =
        personRepository
            .findByUserEmail(email)
            .orElseThrow(() -> new DataNotFoundException("Person not found"));

    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(sessionToken);
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(personConverter.toPersonDTOView(person));
    response.setSuccess(true);

    return response;
  }
}
