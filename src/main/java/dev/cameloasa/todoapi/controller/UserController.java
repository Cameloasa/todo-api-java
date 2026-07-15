package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth/users")
@Validated
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // USER + ADMIN: can view users
  @GetMapping(("/email"))
  public ResponseEntity<UserDTOView> getByEmail( @RequestParam @NotEmpty @Email String email) {
    return ResponseEntity.ok(userService.getByEmail(email));
  }

  @GetMapping("/username")
  public ResponseEntity<UserDTOView> getByUsername( @RequestParam @NotEmpty String username) {
    return ResponseEntity.ok(userService.getByUsername(username));
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserDTOView>> getAll() {
    return ResponseEntity.ok(userService.getAll());
  }

  // ADMIN ONLY: manage users
  @PutMapping("/disable")
  public ResponseEntity<Void> disable( @RequestParam String email) {
    userService.disableEmail(email);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/enable")
  public ResponseEntity<Void> enable( @RequestParam String email) {
    userService.enableEmail(email);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping
  public ResponseEntity<UserDTOView> update(
      @RequestParam String email, @RequestBody @Valid UserDTOForm dtoForm) {
    return ResponseEntity.ok(userService.update(email, dtoForm));
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam String email) {
    userService.delete(email);
    return ResponseEntity.noContent().build();
  }
}
