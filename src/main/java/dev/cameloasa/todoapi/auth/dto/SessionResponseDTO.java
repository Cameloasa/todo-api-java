package dev.cameloasa.todoapi.auth.dto;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import lombok.*;

@Getter
@Setter
public class SessionResponseDTO {

  private String sessionToken;
  private UserDTOView user;
  private PersonDTOView person;
  private boolean success;
}
