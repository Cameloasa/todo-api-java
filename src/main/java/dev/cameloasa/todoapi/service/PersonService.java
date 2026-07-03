package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import java.util.List;

public interface PersonService {
  // Create
  PersonDTOView create(PersonDTOForm dtoForm);

  // Read
  PersonDTOView findById(Long id);

  List<PersonDTOView> findAll();

  // Update
  PersonDTOView update(PersonDTOForm dtoForm);

  // Delete
  void delete(Long id);

  // Extra useful finders
  PersonDTOView findByUsername(String username);

  PersonDTOView findByUserEmail(String email);

  // Search
  List<PersonDTOView> searchByFirstName(String firstName);

  List<PersonDTOView> searchByLastName(String lastName);

  // Business logic
  List<PersonDTOView> findIdlePeople();
}
