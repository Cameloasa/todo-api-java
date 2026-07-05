package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;
  private final PersonConverter personConverter;
  private final UserRepository userRepository;

  public PersonServiceImpl(
      PersonRepository personRepository,
      PersonConverter personConverter,
      UserRepository userRepository) {
    this.personRepository = personRepository;
    this.personConverter = personConverter;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public PersonDTOView create(PersonDTOForm dtoForm) {

    if (dtoForm == null) {
      throw new IllegalArgumentException("PersonDTOForm cannot be null.");
    }

    // User email must be unique in Person
    if (personRepository.findByUserEmail(dtoForm.getUserEmail()).isPresent()) {
      throw new DataDuplicateException("A person is already linked to this user email.");
    }

    // User must exist
    User user =
        userRepository
            .findByEmail(dtoForm.getUserEmail())
            .orElseThrow(
                () ->
                    new DataNotFoundException(
                        "User not found with email: " + dtoForm.getUserEmail()));

    // Convert DTO → Entity
    Person person = personConverter.toPersonEntity(dtoForm);

    // Link Person ↔ User
    person.setUser(user);

    // Save
    Person savedPerson = personRepository.save(person);

    return personConverter.toPersonDTOView(savedPerson);
  }

  @Override
  public PersonDTOView findById(@Nullable Long id) {
    // 1.Find the person by id in repository else throw exception
    @SuppressWarnings("null")
    Person person =
        personRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("The Person does not exist."));
    // 2.Convert to DTO view
    return personConverter.toPersonDTOView(person);
  }

  @Override
  public List<PersonDTOView> findAll() {
    // 1. Retrieve all persons
    List<Person> persons = personRepository.findAll();
    // 2. Convert to DTO views using stream, map and collection
    return persons.stream().map(personConverter::toPersonDTOView).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public PersonDTOView update(PersonDTOForm dtoForm) {

    if (dtoForm == null) {
      throw new IllegalArgumentException("PersonDTOForm cannot be null.");
    }

    @SuppressWarnings("null")
    Person existingPerson =
        personRepository
            .findById(dtoForm.getId())
            .orElseThrow(() -> new DataNotFoundException("Person not found."));

    // If userEmail changed → validate uniqueness
    if (!existingPerson.getUser().getEmail().equals(dtoForm.getUserEmail())
        && personRepository.findByUserEmail(dtoForm.getUserEmail()).isPresent()) {
      throw new DataDuplicateException("Another person is already linked to this user email.");
    }

    // Find user
    User user =
        userRepository
            .findByEmail(dtoForm.getUserEmail())
            .orElseThrow(() -> new DataNotFoundException("User not found."));

    // Update fields
    existingPerson.setFirstName(dtoForm.getFirstName());
    existingPerson.setLastName(dtoForm.getLastName());
    existingPerson.setUser(user);

    Person updated = personRepository.save(existingPerson);

    return personConverter.toPersonDTOView(updated);
  }

  @SuppressWarnings("null")
  @Override
  @Transactional
  public void delete(Long id) {
    // Check if the person exists
    if (!personRepository.existsById(id)) {
      throw new DataNotFoundException("Person not found with id: " + id);
    }
    // Delete the person
    personRepository.deleteById(id);
  }

  @Override
  public PersonDTOView findByUserEmail(String email) {
    Person person =
        personRepository
            .findByUserEmail(email)
            .orElseThrow(
                () -> new DataNotFoundException("Person not found with user email: " + email));

    return personConverter.toPersonDTOView(person);
  }

  @Override
  public List<PersonDTOView> searchByFirstName(String firstName) {
    return personRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
        .map(personConverter::toPersonDTOView)
        .toList();
  }

  @Override
  public List<PersonDTOView> searchByLastName(String lastName) {
    return personRepository.findByLastNameContainingIgnoreCase(lastName).stream()
        .map(personConverter::toPersonDTOView)
        .toList();
  }

  @Override
  public List<PersonDTOView> findIdlePeople() {
    return personRepository.findIdlePeople().stream()
        .map(personConverter::toPersonDTOView)
        .toList();
  }
}
