package dev.cameloasa.todoapi.domanin.dto;

import lombok.*;

@Getter
@Setter
public class SessionResponseDTO {

  private String sessionToken;
  private UserDTOView user;
  private PersonDTOView person;
  private boolean success;
}
