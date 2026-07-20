package dev.cameloasa.todoapi.domanin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetForm {

  @NotBlank(message = "Password is required")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#@$!%*?&])[A-Za-z\\d#@$!%*?&]{8,}$",
      message = "Password must contain upper, lower, digit, special char and be min 8 chars")
  private String password;
}
