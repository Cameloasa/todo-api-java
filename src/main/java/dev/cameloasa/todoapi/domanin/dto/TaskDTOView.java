package dev.cameloasa.todoapi.domanin.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskDTOView {
  private Long id;
  private String title;
  private String description;
  private LocalDate deadline;
  private boolean done;
}
