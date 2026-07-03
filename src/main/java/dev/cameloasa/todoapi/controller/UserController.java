package dev.cameloasa.todoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import dev.cameloasa.todoapi.service.UserService;

@CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend URL
@RequestMapping("api/v1/users")
@RestController
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registers a new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<UserDTOView>doRegister(@RequestBody @Valid UserDTOForm dtoForm) {
        System.out.println("DTO Form: " + dtoForm);
        UserDTOView responseBody = userService.register(dtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<UserDTOView> doUserByEmail(
            @RequestParam
            @NotEmpty
            @NotNull
            @Email
            //@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format") //simple email format
            String email) {
        System.out.println(">>>>>>> getUserByEmail has been executed");
        System.out.println("Email: " + email);

        UserDTOView responseBody = userService.getByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    @PutMapping("/disable")
    public ResponseEntity<Void> doDisableUserByEmail(@RequestParam String email) {
        System.out.println(">>>>>>> Disable User ByEmail has been executed");
        userService.disableEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/enable")
    public ResponseEntity<Void> doEnableUserByEmail(@RequestParam String email) {
        System.out.println(">>>>>>> Enable User ByEmail has been executed");
        userService.enableEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
