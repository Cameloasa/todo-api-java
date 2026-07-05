package dev.cameloasa.todoapi.auth.dto;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.dto.UserDTOView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTOView {

  private UserDTOView user;
  private PersonDTOView person;
  private boolean success;
}
