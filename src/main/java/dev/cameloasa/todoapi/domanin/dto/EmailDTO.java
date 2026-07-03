package dev.cameloasa.todoapi.domanin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmailDTO {

  @NotBlank(message = "Recipient email is required.")
  @Email(message = "Invalid email format.")
  private String to;

  @NotBlank(message = "Subject is required.")
  @Size(min = 3, max = 255, message = "Subject must be between 3 and 255 characters.")
  private String subject;

  @NotBlank(message = "Email content cannot be empty.")
  private String html;
}
