package dev.cameloasa.todoapi.domanin.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PersonDTOView {
  private Long id;
  private String firstName;
  private String lastName;
  private String userEmail;
  private List<TaskDTOView> tasks;
}
