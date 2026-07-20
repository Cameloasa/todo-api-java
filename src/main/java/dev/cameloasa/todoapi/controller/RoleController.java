package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.service.RoleService;
import dev.cameloasa.todoapi.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend URL
@RequestMapping("/auth/roles")
@RestController
public class RoleController {
  private final RoleService roleService;
  private final UserService userService;

  public RoleController(RoleService roleService, UserService userService) {
    this.roleService = roleService;
    this.userService = userService;
  }

  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @GetMapping
  public ResponseEntity<List<RoleDTOView>> doGetAllRoles() {
    List<RoleDTOView> responseBody = roleService.getAll();
    return ResponseEntity.ok(responseBody);
  }

  // SUPERADMIN: create new role
  @PreAuthorize("hasRole('SUPERADMIN')")
  @PostMapping("/create")
  public ResponseEntity<RoleDTOView> doCreateRole(@Valid @RequestBody RoleDTOForm dtoForm) {
    RoleDTOView created = roleService.create(dtoForm);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  // SUPERADMIN: assign new role
  @PutMapping("/assign")
  @PreAuthorize("hasRole('SUPERADMIN')")
  public ResponseEntity<UserDTOView> updateRoles(
      @Valid @RequestParam String email, @RequestBody List<Long> roleIds) {

    return ResponseEntity.ok(userService.updateRoles(email, roleIds));
  }
}
