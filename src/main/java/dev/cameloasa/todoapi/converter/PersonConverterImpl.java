package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonConverterImpl implements PersonConverter {

  private final TaskConverter taskConverter;

  public PersonConverterImpl(TaskConverter taskConverter) {
    this.taskConverter = taskConverter;
  }

  @Override
  public PersonDTOView toPersonDTOView(Person entity) {
    if (entity == null) return null;

    return PersonDTOView.builder()
        .id(entity.getId())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .username(entity.getUsername())
        .userEmail(entity.getUser() != null ? entity.getUser().getEmail() : null)
        .tasks(
            entity.getTasks() == null
                ? null
                : entity.getTasks().stream().map(taskConverter::toTaskDTOView).toList())
        .build();
  }

  @Override
  public Person toPersonEntity(PersonDTOForm dtoForm) {
    if (dtoForm == null) return null;

    return Person.builder()
        .id(dtoForm.getId())
        .firstName(dtoForm.getFirstName())
        .lastName(dtoForm.getLastName())
        .username(dtoForm.getUsername())
        // userEmail does not map here because the email is part of the User entity, not the Person
        // entity. The mapping of userEmail should be handled in the service layer where the User
        // entity is available.
        // user-search in user entity should be done in the service layer, not in the converter.
        .build();
  }
}
