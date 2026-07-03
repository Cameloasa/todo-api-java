package dev.cameloasa.todoapi.domanin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RoleDTOForm {

  @PositiveOrZero(message = "\"Id must be a positive number or zero\".")
  private Long id;

  @NotBlank(message = "Name is required.")
  @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters.")
  private String name;
}
