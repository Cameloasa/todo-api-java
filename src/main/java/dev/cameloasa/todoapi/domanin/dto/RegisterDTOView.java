package dev.cameloasa.todoapi.domanin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTOView {

  private UserDTOView user;
  private PersonDTOView person;
  private boolean success;
}
