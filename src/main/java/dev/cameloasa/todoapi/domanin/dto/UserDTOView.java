package dev.cameloasa.todoapi.domanin.dto;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTOView {
  private String email;
  private boolean expired;
  private Set<RoleDTOView> roles;
}
