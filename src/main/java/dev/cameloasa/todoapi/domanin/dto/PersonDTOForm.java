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
public class PersonDTOForm {

  private Long id; // optional, use to update person, if null, create new person

  @NotBlank(message = "First name is required.")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
  private String firstName;

  @NotBlank(message = "Last name is required.")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
  private String lastName;

  @NotBlank(message = "Username is required.")
  @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters.")
  private String username;

  @NotBlank(message = "User email is required.")
  @Email(message = "Invalid email format.")
  private String userEmail;
}
