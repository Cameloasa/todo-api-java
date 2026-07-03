package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;

public interface PersonConverter {
  PersonDTOView toPersonDTOView(Person entity);

  Person toPersonEntity(PersonDTOForm dtoForm);
}
