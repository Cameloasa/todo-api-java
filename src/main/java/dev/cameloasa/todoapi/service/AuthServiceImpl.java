package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.AuthConverter;
import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.UserConverter;
import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.SessionResponseDTO;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.domanin.entity.SessionEntity;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.exception.InvalidCredentialsException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserServiceImpl userService;
  private final PersonServiceImpl personService;
  private final EmailServiceImpl emailService;
  private final SessionService sessionService;

  private final AuthConverter authConverter;
  private final UserConverter userConverter;
  private final PersonConverter personConverter;

  private final UserRepository userRepository;
  private final PersonRepository personRepository;

  private final PasswordEncoder passwordEncoder;

  // ---------------------------------------------------------
  // Helpers (Login Me + Session)
  // ---------------------------------------------------------

  private void addSessionCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie("session_token", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24);
    response.addCookie(cookie);
  }

  private UserDTOView safeFindUserByEmail(String email) {
    return userRepository.findByEmail(email).map(userConverter::toUserDTOView).orElse(null);
  }

  private PersonDTOView safeFindPersonByEmail(String email) {
    return personRepository
        .findByUserEmail(email)
        .map(personConverter::toPersonDTOView)
        .orElse(null);
  }

  // ---------------------------------------------------------
  // Register (User + Person + Session)
  // ---------------------------------------------------------
  @Override
  public SessionResponseDTO register(RegisterDTOForm dto) {

    // 1. Convert RegisterDTOForm → UserDTOForm
    UserDTOForm userForm = authConverter.toUserDTOForm(dto);

    // 2. Create user
    UserDTOView user = userService.register(userForm);

    // 3. Convert RegisterDTOForm → PersonDTOForm
    PersonDTOForm personForm = authConverter.toPersonDTOForm(dto);

    // 4. Create person
    PersonDTOView person = personService.create(personForm);

    // 5. Send email
    emailService.sendRegistrationEmail(user.getEmail());

    // 6. Create sesiune
    String sessionToken = sessionService.createSession(user.getEmail());

    // 7. Returnează user + person + session
    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(sessionToken);
    response.setUser(user);
    response.setPerson(person);
    response.setSuccess(true);

    return response;
  }

  // ---------------------------------------------------------
  // Login (User + Person + Session)
  // ---------------------------------------------------------
  @Override
  public SessionResponseDTO login(LoginDTOForm dto, HttpServletResponse response) {

    // 1. Find user by email
    User user =
        userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    // 2. Verify password
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid email or password");
    }

    // 3. Find person
    PersonDTOView person = personService.findByUserEmail(user.getEmail());

    // 4. Create session
    String token = sessionService.createSession(user.getEmail());

    // 5. Set cookie
    addSessionCookie(response, token);

    // 6. Return user + person + token
    SessionResponseDTO session = new SessionResponseDTO();
    session.setSessionToken(token);
    session.setUser(userConverter.toUserDTOView(user));
    session.setPerson(person);
    session.setSuccess(true);

    return session;
  }

  // ---------------------------------------------------------
  // Logout
  // ---------------------------------------------------------
  @Override
  public void logout(String sessionToken) {

    if (!sessionService.isValid(sessionToken)) {
      throw new InvalidCredentialsException("Session not found");
    }

    sessionService.deleteSession(sessionToken);
  }

  // ---------------------------------------------------------
  // Me (User + Person + Session)
  // ---------------------------------------------------------
  @Override
  public SessionResponseDTO me(String sessionToken) {

    // 1. Validate token presence
    if (sessionToken == null || sessionToken.isBlank()) {
      throw new InvalidCredentialsException("Session token is required");
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

    // 3. Fetch user safely

    UserDTOView user = safeFindUserByEmail(email);
    if (user == null) throw new InvalidCredentialsException("User not found");

    // 4. Fetch person safely (optional)
    PersonDTOView person = safeFindPersonByEmail(email);
    if (person == null) throw new DataNotFoundException(email);

    // 7. Build response
    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(sessionToken);
    response.setUser(user);
    response.setPerson(person);
    response.setSuccess(true);

    return response;
  }
}
