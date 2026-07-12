package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.AuthConverter;
import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.PersonConverterImpl;
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
import dev.cameloasa.todoapi.exception.InvalidCredentialsException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final PersonService personService;
  private final SessionService sessionService;
  private final AuthConverter authConverter;
  private final PasswordEncoder passwordEncoder;
  private final UserConverter userConverter;
  private final PersonConverter personConverter;
  private final UserRepository userRepository;
  private final PersonRepository personRepository;

  // ---------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------
  private User findUserByEmailOrUsername(LoginDTOForm dto) {

    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      return userRepository
          .findByEmail(dto.getEmail())
          .orElseThrow(() -> new InvalidCredentialsException("Invalid email"));
    }

    if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
      return userRepository
          .findByUsername(dto.getUsername())
          .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));
    }

    throw new InvalidCredentialsException("Email or username must be provided");
  }
   
  private UserDTOView safeFindUserByEmail(String email) {
    return userRepository.findById(email)
        .map(userConverter::toUserDTOView)
        .orElse(null);
}
private PersonDTOView safeFindPersonByEmail(String email) {
    return personRepository.findByUserEmail(email)
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

    // 2. Creează user
    UserDTOView user = userService.register(userForm);

    // 3. Convert RegisterDTOForm → PersonDTOForm
    PersonDTOForm personForm = authConverter.toPersonDTOForm(dto);

    // 4. Creează person
    PersonDTOView person = personService.create(personForm);

    // 5. Creează sesiune
    String sessionToken = sessionService.createSession(user.getEmail());

    // 6. Returnează user + person + session
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
  public SessionResponseDTO login(LoginDTOForm dto) {

    // 1. Găsim user după email sau username
    User user = findUserByEmailOrUsername(dto);

    // 2. Verificăm parola
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid password");
    }

    // 3. Găsim person
    PersonDTOView person = personService.findByUserEmail(user.getEmail());

    // 4. Creăm sesiune
    String token = sessionService.createSession(user.getEmail());

    // 5. Returnăm user + person + token
    SessionResponseDTO response = new SessionResponseDTO();
    response.setSessionToken(token);
    response.setUser(userConverter.toUserDTOView(user));
    response.setPerson(person);
    response.setSuccess(true);
    return response;
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

    System.out.println("=== /auth/me CALLED ===");
    System.out.println("TOKEN PRIMIT: " + sessionToken);

      // 1. Validate token presence
      if (sessionToken == null || sessionToken.isBlank()) {
          throw new InvalidCredentialsException("Session token is required");
      }

      // 2. Fetch session object
      SessionEntity session =
          sessionService
              .getSession(sessionToken)
              .orElseThrow(() -> new InvalidCredentialsException("Invalid session token"));

    System.out.println("SESSION GASITA: " + session.getToken());
    System.out.println("SESSION EMAIL: " + session.getUserEmail());
    System.out.println("SESSION CREATED_AT: " + session.getCreatedAt());
    System.out.println("SESSION EXPIRES_AT: " + session.getExpiresAt());
    System.out.println("NOW: " + System.currentTimeMillis());
    System.out.println("VALID SESSION: " + sessionService.isValid(sessionToken));

      // 3. Validate session (expired, revoked, etc.)
      if (!sessionService.isValid(sessionToken)) {
          throw new InvalidCredentialsException("Session expired");
      }

      // 4. Extract email from session
      String email = session.getUserEmail();
      System.out.println("EMAIL DIN SESIUNE: [" + email + "]");

      // 3. Fetch user safely
      System.out.println("CAUT USER CU EMAIL: " + email);
      UserDTOView user = safeFindUserByEmail(email);
    if (user == null) {
      System.out.println("USER NU A FOST GASIT → 403");
        throw new InvalidCredentialsException("User not found");
    }

    // 4. Fetch person safely (optional)
    System.out.println("CAUT PERSON CU EMAIL: " + email);
    PersonDTOView person = safeFindPersonByEmail(email);
    if (person == null) {
        System.out.println("PERSON NU A FOST GASIT → NULL (OK)");
    } else {
        System.out.println("PERSON GASIT: " + person.getUserEmail());
    }

      // 7. Build response
      SessionResponseDTO response = new SessionResponseDTO();
      response.setSessionToken(sessionToken);
      response.setUser(user);
      response.setPerson(person);
      response.setSuccess(true);

      System.out.println("=== /auth/me COMPLETED ===");
      return response;
  }


}
