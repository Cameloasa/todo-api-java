package dev.cameloasa.todoapi.unit.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.InvalidCredentialsException;
import dev.cameloasa.todoapi.unit.fixtures.AuthFixture;
import dev.cameloasa.todoapi.unit.fixtures.PersonFixture;
import dev.cameloasa.todoapi.unit.fixtures.SessionFixture;
import dev.cameloasa.todoapi.unit.fixtures.UnitTestBase;
import dev.cameloasa.todoapi.unit.fixtures.UserFixture;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest extends UnitTestBase {

  // ---------------------------------------------------------
  // TEST : register
  // ---------------------------------------------------------
  @SuppressWarnings("null")
  @Test
  void register_with_valid_data_should_create_user_and_person() {
    var dto = AuthFixture.sampleRegisterForm();
    var role = AuthFixture.sampleRole();

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);

    when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);

    when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

    when(userConverter.toUserDTOView(any(User.class))).thenReturn(UserFixture.sampleUserDTOView());

    var savedUser = AuthFixture.sampleUserWithEncodedPassword();
    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    when(personConverter.toPersonDTOView(any(Person.class)))
        .thenReturn(PersonFixture.samplePersonDTOView(savedUser));

    var savedPerson = AuthFixture.samplePerson(savedUser);
    when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

    // email service mock
    when(emailService.sendRegistrationEmail(savedUser.getEmail())).thenReturn(HttpStatus.OK);

    var result = authService.register(dto);

    assertNotNull(result);
    assertEquals(dto.getEmail(), result.getUser().getEmail());
    assertEquals(dto.getUsername(), result.getUser().getUsername());
    assertEquals(dto.getFirstName(), result.getPerson().getFirstName());
    assertEquals(dto.getLastName(), result.getPerson().getLastName());
    assertTrue(result.isSuccess());
  }

  // ---------------------------------------------------------
  // TEST : duplicate email
  // ---------------------------------------------------------
  @Test
  void register_with_duplicate_email_should_throw() {
    var dto = AuthFixture.sampleRegisterForm();

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

    assertThrows(DataDuplicateException.class, () -> authService.register(dto));
  }

  // ---------------------------------------------------------
  // TEST : duplicate username
  // ---------------------------------------------------------
  @Test
  void register_with_duplicate_username_should_throw() {
    var dto = AuthFixture.sampleRegisterForm();

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false); // email OK

    when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true); // username duplicat

    assertThrows(DataDuplicateException.class, () -> authService.register(dto));
  }

  // ---------------------------------------------------------
  // TEST : login
  // ---------------------------------------------------------

  @Test
void login_should_work() {
    var dto = AuthFixture.sampleLoginForm();
    var user = AuthFixture.sampleUser();
    var person = AuthFixture.samplePerson(user);
    var session = SessionFixture.validSession(user);

    HttpServletResponse response = mock(HttpServletResponse.class);

    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
    when(sessionService.createSession(user.getEmail())).thenReturn(session.getToken());
    when(personRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(person));

    var result = authService.login(dto, response);

    assertEquals(session.getToken(), result.getSessionToken());
}

  @Test
void login_with_valid_username_should_return_session() {
    var dto = AuthFixture.sampleLoginForm();
    var user = AuthFixture.sampleUserWithEncodedPassword();
    var person = AuthFixture.samplePerson(user);
    var session = AuthFixture.sampleSession(user);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // user lookup
    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(user));

    // password check
    when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            .thenReturn(true);

    // person lookup
    when(personRepository.findByUserEmail(user.getEmail()))
            .thenReturn(Optional.of(person));

    // session creation
    when(sessionService.createSession(user.getEmail()))
            .thenReturn(session.getToken());

    // converters
    when(userConverter.toUserDTOView(user))
            .thenReturn(UserFixture.sampleUserDTOView());

    when(personConverter.toPersonDTOView(person))
            .thenReturn(PersonFixture.samplePersonDTOView(user));

    var result = authService.login(dto, response);

    assertNotNull(result);
    assertEquals(session.getToken(), result.getSessionToken());
}

  // ---------------------------------------------------------
  // TEST : wrong password
  // ---------------------------------------------------------
  @Test
void login_with_wrong_password_should_throw() {
    var dto = AuthFixture.sampleLoginForm();
    var user = AuthFixture.sampleUserWithEncodedPassword();

    HttpServletResponse response = mock(HttpServletResponse.class);

    // user lookup
    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(user));

    // password check → WRONG PASSWORD
    when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            .thenReturn(false);

    assertThrows(InvalidCredentialsException.class,
            () -> authService.login(dto, response));
}


  

  // ---------------------------------------------------------
  // TEST : me expired session
  // ---------------------------------------------------------
  @Test
  void me_with_expired_session_should_throw() {
    var user = AuthFixture.sampleUser();
    var session = SessionFixture.validSession(user);

    session.setExpiresAt(System.currentTimeMillis() - 1000);

    when(sessionService.getSession("expired-token")).thenReturn(Optional.of(session));

    assertThrows(RuntimeException.class, () -> authService.me("expired-token"));
  }

  // ---------------------------------------------------------
  // TEST : me invalid token
  // ---------------------------------------------------------
  @Test
  void me_with_invalid_token_should_throw() {
    when(sessionService.getSession("invalid-token")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> authService.me("invalid-token"));
  }

  // ---------------------------------------------------------
  // TEST : logout
  // ---------------------------------------------------------
  @Test
  void logout_should_delete_session() {

    when(sessionService.isValid("valid-token")).thenReturn(true);

    doNothing().when(sessionService).deleteSession("valid-token");

    authService.logout("valid-token");

    verify(sessionService, times(1)).deleteSession("valid-token");
  }

  // ---------------------------------------------------------
  // TEST : logout invalid token
  // ---------------------------------------------------------
  @Test
  void logout_with_invalid_token_should_throw() {

    when(sessionService.isValid("invalid-token")).thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> authService.logout("invalid-token"));
  }
}
