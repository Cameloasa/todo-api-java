package dev.cameloasa.todoapi.auth.controller;



import org.springframework.web.bind.annotation.*;

import dev.cameloasa.todoapi.auth.dto.LoginDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOForm;
import dev.cameloasa.todoapi.auth.dto.RegisterDTOView;
import dev.cameloasa.todoapi.auth.dto.SessionResponseDTO;
import dev.cameloasa.todoapi.auth.service.AuthService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterDTOView register(@RequestBody RegisterDTOForm dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public SessionResponseDTO login(@RequestBody LoginDTOForm dto) {
        return authService.login(dto);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("X-Session-Token") String sessionToken) {
        authService.logout(sessionToken);
    }

    @GetMapping("/me")
    public SessionResponseDTO me(@RequestHeader("X-Session-Token") String sessionToken) {
        return authService.me(sessionToken);
    }
}

