package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
@RestController
@Validated
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // REGISTER
  @PostMapping
  public ResponseEntity<UserDTOView> doRegister(@RequestBody @Valid UserDTOForm dtoForm) {
    UserDTOView responseBody = userService.register(dtoForm);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
  }

  // GET USER BY EMAIL
  @GetMapping
  public ResponseEntity<UserDTOView> doUserByEmail(@RequestParam @NotEmpty @Email String email) {

    UserDTOView responseBody = userService.getByEmail(email);
    return ResponseEntity.ok(responseBody);
  }

  // DISABLE USER
  @PutMapping("/disable")
  public ResponseEntity<Void> doDisableUserByEmail(@RequestParam String email) {
    userService.disableEmail(email);
    return ResponseEntity.noContent().build();
  }

  // ENABLE USER
  @PutMapping("/enable")
  public ResponseEntity<Void> doEnableUserByEmail(@RequestParam String email) {
    userService.enableEmail(email);
    return ResponseEntity.noContent().build();
  }
}
