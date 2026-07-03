package dev.cameloasa.todoapi.domanin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskDTOForm {

  @PositiveOrZero(message = "Id must be zero or a positive number.")
  private Long id;

  @NotBlank(message = "Title is required.")
  @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
  private String title;

  @Size(max = 500, message = "Description cannot exceed 500 characters.")
  private String description;

  @NotNull(message = "Deadline is required.")
  private LocalDate deadline;

  @NotNull(message = "Done status is required.")
  private boolean done;

  @NotNull(message = "Person ID is required.")
  @Positive(message = "Person ID must be a positive number.")
  private Long personId;
}
