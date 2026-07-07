package dev.cameloasa.todoapi.auth.controller;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOView;
import dev.cameloasa.todoapi.auth.dto.SessionResponseDTO;
import dev.cameloasa.todoapi.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  // ---------------------------------------------------------
  // REGISTER → 201 Created
  // ---------------------------------------------------------
  @PostMapping("/register")
  public ResponseEntity<RegisterDTOView> register(@Valid @RequestBody RegisterDTOForm dto) {
    RegisterDTOView response = authService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // ---------------------------------------------------------
  // LOGIN → 200 OK
  // ---------------------------------------------------------
  @PostMapping("/login")
  public ResponseEntity<SessionResponseDTO> login(
      @RequestBody LoginDTOForm dto, HttpServletResponse httpResponse) {

    SessionResponseDTO response = authService.login(dto);

    Cookie cookie = new Cookie("session_token", response.getSessionToken());
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24); // 1 zi
    httpResponse.addCookie(cookie);

    return ResponseEntity.ok(response);
  }

  // ---------------------------------------------------------
  // LOGOUT → 200 OK (no body)
  // ---------------------------------------------------------
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("X-Session-Token") String sessionToken) {
    authService.logout(sessionToken);
    return ResponseEntity.ok().build();
  }

  // ---------------------------------------------------------
  // ME → 200 OK
  // ---------------------------------------------------------
  @GetMapping("/me")
  public ResponseEntity<SessionResponseDTO> me(
      @RequestHeader("X-Session-Token") String sessionToken) {
    SessionResponseDTO response = authService.me(sessionToken);
    return ResponseEntity.ok(response);
  }
}
