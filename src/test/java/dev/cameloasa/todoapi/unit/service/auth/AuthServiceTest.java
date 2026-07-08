package dev.cameloasa.todoapi.unit.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;

import dev.cameloasa.todoapi.unit.fixtures.SessionFixture;
import dev.cameloasa.todoapi.unit.fixtures.UnitTestBase;
import dev.cameloasa.todoapi.unit.fixtures.AuthFixture;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest extends UnitTestBase{

// ---------------------------------------------------------
  // TEST : register
  // ---------------------------------------------------------
@SuppressWarnings("null")
@Test
void register_with_valid_data_should_create_user_and_person() {
    var dto = AuthFixture.sampleRegisterForm();
    var role = AuthFixture.sampleRole();

    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.empty());
    when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.empty());
    when(roleRepository.findByName("USER"))
            .thenReturn(Optional.of(role));

    when(passwordEncoder.encode(dto.getPassword()))
            .thenReturn("encoded-password");

    var savedUser = AuthFixture.sampleUserWithEncodedPassword();
    when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

    var savedPerson = AuthFixture.samplePerson(savedUser);
    when(personRepository.save(any(Person.class)))
            .thenReturn(savedPerson);

    var result = authService.register(dto);

    assertNotNull(result);
    assertEquals(dto.getEmail(), result.getUser().getEmail());
    assertEquals(dto.getUsername(), result.getUser().getUsername());
}

// ---------------------------------------------------------
  // TEST : duplicate email
  // ---------------------------------------------------------
@Test
void register_with_duplicate_email_should_throw() {
    var dto = AuthFixture.sampleRegisterForm();
    var existingUser = AuthFixture.sampleUser();

    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(existingUser));

    assertThrows(RuntimeException.class, () -> authService.register(dto));
}

// ---------------------------------------------------------
  // TEST : duplicate username
  // ---------------------------------------------------------
@Test
void register_with_duplicate_username_should_throw() {
    var dto = AuthFixture.sampleRegisterForm();

    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.empty());
    when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.of(AuthFixture.sampleUser()));

    assertThrows(RuntimeException.class, () -> authService.register(dto));
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

    when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.of(user));
    when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            .thenReturn(true);
    when(sessionService.createSession(user.getEmail()))
            .thenReturn(session.getToken());
    when(personRepository.findByUserEmail(user.getEmail()))
            .thenReturn(Optional.of(person));

    var result = authService.login(dto);

    assertEquals(session.getToken(), result.getSessionToken());
}

// ---------------------------------------------------------
  // TEST : valid username return session
  // ---------------------------------------------------------
@Test
void login_with_valid_username_should_return_session() {
    var dto = AuthFixture.sampleLoginForm();
    var user = AuthFixture.sampleUser();
    var session = AuthFixture.sampleSession(user);

    when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.of(user));

    when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            .thenReturn(true);

    when(sessionService.createSession(user.getEmail()))
            .thenReturn(session.getToken());

    var result = authService.login(dto);

    assertNotNull(result);
    assertEquals(session.getToken(), result.getSessionToken());
}



// ---------------------------------------------------------
  // TEST : wrong password
  // ---------------------------------------------------------
@Test
void login_with_wrong_password_should_throw() {
    var dto = AuthFixture.sampleLoginForm();
    var user = AuthFixture.sampleUser();

    when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.of(user));
    when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            .thenReturn(false);

    assertThrows(RuntimeException.class, () -> authService.login(dto));
}

// ---------------------------------------------------------
  // TEST : non-existent user
  // ---------------------------------------------------------
@Test
void login_with_nonexistent_user_should_throw() {
    var dto = AuthFixture.sampleLoginForm();

    when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> authService.login(dto));
}


// ---------------------------------------------------------
  // TEST : me
  // ---------------------------------------------------------
  @Test
void me_with_valid_session_should_return_user_and_person() {
    var user = AuthFixture.sampleUser();
    var person = AuthFixture.samplePerson(user);
    var session = SessionFixture.validSession(user);

    when(sessionService.getSession("valid-token"))
            .thenReturn(Optional.of(session));
    when(userRepository.findByEmail(session.getUserEmail()))
            .thenReturn(Optional.of(user));
    when(personRepository.findByUserEmail(user.getEmail()))
            .thenReturn(Optional.of(person));

    var result = authService.me("valid-token");

    assertNotNull(result);
    assertEquals(user.getEmail(), result.getUser().getEmail());
    assertEquals(user.getUsername(), result.getUser().getUsername());
    assertEquals(person.getFirstName(), result.getPerson().getFirstName());
    assertTrue(result.isSuccess());
}

// ---------------------------------------------------------
  // TEST : me expired session
  // ---------------------------------------------------------
@Test
void me_with_expired_session_should_throw() {
    var user = AuthFixture.sampleUser();
    var session = SessionFixture.validSession(user);

    session.setExpiresAt(System.currentTimeMillis() - 1000);

    when(sessionService.getSession("expired-token"))
            .thenReturn(Optional.of(session));

    assertThrows(RuntimeException.class, () -> authService.me("expired-token"));
}


// ---------------------------------------------------------
  // TEST : me invalid token
  // ---------------------------------------------------------
@Test
void me_with_invalid_token_should_throw() {
    when(sessionService.getSession("invalid-token"))
            .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> authService.me("invalid-token"));
}

// ---------------------------------------------------------
  // TEST : logout
  // ---------------------------------------------------------
@SuppressWarnings("null")
@Test
void logout_should_delete_session() {
    var user = AuthFixture.sampleUser();
    var session = SessionFixture.validSession(user);

    when(sessionService.getSession("valid-token"))
            .thenReturn(Optional.of(session));

    doNothing().when(sessionService).deleteSession(session.getToken());

    // metoda nu returnează nimic, deci doar o apelăm
    authService.logout("valid-token");

    // verificăm că deleteSession a fost apelat
    verify(sessionService, times(1)).deleteSession(session.getToken());
}

// ---------------------------------------------------------
  // TEST : logout invalid token
  // ---------------------------------------------------------
@Test
void logout_with_invalid_token_should_throw() {
    when(sessionService.getSession("invalid-token"))
            .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> authService.logout("invalid-token"));
}






}

