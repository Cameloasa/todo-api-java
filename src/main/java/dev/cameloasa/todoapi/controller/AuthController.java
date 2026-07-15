package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.LoginDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.domanin.dto.SessionResponseDTO;
import dev.cameloasa.todoapi.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  public ResponseEntity<SessionResponseDTO> register(@Valid @RequestBody RegisterDTOForm dto) {
    SessionResponseDTO response = authService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
  

  // ---------------------------------------------------------
  // LOGIN → 200 OK
  // ---------------------------------------------------------
    @PostMapping("/login")
  public ResponseEntity<SessionResponseDTO> login(
      @Valid @RequestBody LoginDTOForm dto,
      HttpServletResponse response
  ) {
      SessionResponseDTO session = authService.login(dto, response);
      return ResponseEntity.ok(session);
  }

  // ---------------------------------------------------------
  // LOGOUT → 200 OK (no body)
  // ---------------------------------------------------------
  @PostMapping("/logout")
  public ResponseEntity<Void> logout( @RequestHeader("X-Session-Token") String sessionToken) {
    authService.logout(sessionToken); 
    return ResponseEntity.ok().build();
  }

  // ---------------------------------------------------------
  // ME → 200 OK
  // ---------------------------------------------------------
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('USER','ADMIN','SUPERADMIN')")
  public ResponseEntity<SessionResponseDTO> me(
      @RequestHeader("X-Session-Token") String sessionToken) {
    SessionResponseDTO response = authService.me(sessionToken);
    return ResponseEntity.ok(response);
  }
}
