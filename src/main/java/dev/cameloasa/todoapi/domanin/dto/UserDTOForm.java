package dev.cameloasa.todoapi.domanin.dto;

import jakarta.validation.constraints.*;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTOForm {

  @NotBlank(message = "Email is required.")
  @Email(message = "Invalid email format.")
  private String email;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be at least 8 characters.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "Password must contain at least one uppercase letter, one lowercase letter, one number, one special character.")
  private String password;

  @NotBlank(message = "Username is required.")
  @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters.")
  private String username;

  @NotNull(message = "Roles cannot be null.")
  @Size(min = 1, message = "User must have at least one role.")
  private Set<Long> roleIds; // just the IDs of the roles, not the full RoleDTOView objects

  private boolean expired;
}
