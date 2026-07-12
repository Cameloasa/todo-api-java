package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.RoleDTOForm;
import dev.cameloasa.todoapi.domanin.dto.RoleDTOView;
import dev.cameloasa.todoapi.service.RoleService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend URL
@RequestMapping("/auth/roles")
@RestController
public class RoleController {
  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public ResponseEntity<List<RoleDTOView>> doGetAllRoles() {
    List<RoleDTOView> responseBody = roleService.getAll();
    return ResponseEntity.ok(responseBody);
  }

  // SUPERADMIN: create new role
    @PostMapping("/create")
    public ResponseEntity<RoleDTOView> doCreateRole(@RequestBody RoleDTOForm dtoForm) {
        RoleDTOView created = roleService.create(dtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
