package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.UserConverter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;
  private final PersonConverter personConverter;
  private final UserRepository userRepository;
  private final UserConverter userConverter;

  @Autowired
  public PersonServiceImpl(
      PersonRepository personRepository,
      PersonConverter personConverter,
      UserRepository userRepository,
      UserConverter userConverter) {
    this.personRepository = personRepository;
    this.personConverter = personConverter;
    this.userRepository = userRepository;
    this.userConverter = userConverter;
  }

  @Override
  @Transactional
  public PersonDTOView create(PersonDTOForm dtoForm) {
    // Check the param
    if (dtoForm == null) throw new IllegalArgumentException("This Form is cannot be accepted.");
    // 2. Check if the person exist in the database
    boolean personExists = personRepository.existsById(dtoForm.getId());
    // 3.if person exist throw an Exception - Data Duplicate Exception
    if (personExists) {
      throw new DataDuplicateException("The Person already exists.");
    }
    // 4. Convert PersonDTOForm to Person entity using PersonConverter
    Person person = personConverter.toPersonEntity(dtoForm);
    // 5. Save the Person to the database
    Person savedPerson = personRepository.save(person);
    // 6. Convert saved Person entity to PersonDTOView using PersonConverter
    return personConverter.toPersonDTOView(savedPerson);
  }

  @Override
  public PersonDTOView findById(Long id) {
    // 1.Find the person by id in repository else throw exception
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
    // Check the param
    if (dtoForm == null) throw new IllegalArgumentException("This Form is not accepted.");
    // Find the existing person
    Person existingPerson =
        personRepository
            .findById(dtoForm.getId())
            .orElseThrow(() -> new DataNotFoundException("The Person does not exist."));
    // Update the person's details
    existingPerson.setName(dtoForm.getName());
    // Update associated user if email is provided
    if (dtoForm.getUserEmail() != null && userRepository.existsByEmail(dtoForm.getUserEmail())) {
      User user =
          userRepository
              .findByEmail(dtoForm.getUserEmail())
              .orElseThrow(() -> new DataNotFoundException("The User does not exist."));
      existingPerson.setUser(user);
    }
    // Save the updated person
    Person updatedPerson = personRepository.save(existingPerson);
    // Convert to DTO view and return
    return personConverter.toPersonDTOView(updatedPerson);
  }

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
}
