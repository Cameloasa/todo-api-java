package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.*;
import dev.cameloasa.todoapi.service.PersonService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth/persons")
@RestController
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }
   // admin has acces to endpoints
  // FIND BY ID
  @GetMapping("/{id}")
  public ResponseEntity<PersonDTOView> doFindPersonById(@Valid @PathVariable Long id) {
    PersonDTOView responseBody = personService.findById(id);
    return ResponseEntity.ok(responseBody);
  }

  // FIND ALL
  @GetMapping
  public ResponseEntity<List<PersonDTOView>> doFindAll() {
    List<PersonDTOView> responseBody = personService.findAll();
    return ResponseEntity.ok(responseBody);
  }

  // UPDATE
  @PatchMapping
  public ResponseEntity<PersonDTOView> doUpdate(@Valid @RequestBody PersonDTOForm dtoForm) {
    PersonDTOView updated = personService.update(dtoForm);
    return ResponseEntity.ok(updated);
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> doDelete(@Valid @PathVariable Long id) {
    personService.delete(id);
    return ResponseEntity.noContent().build();
  }

  // FIND BY USER EMAIL
  @GetMapping("/user-email/{email}")
  public ResponseEntity<PersonDTOView> doFindByUserEmail(@Valid @PathVariable String email) {
    PersonDTOView responseBody = personService.findByUserEmail(email);
    return ResponseEntity.ok(responseBody);
  }

  // SEARCH BY FIRST NAME
  @GetMapping("/search/first-name/{firstName}")
  public ResponseEntity<List<PersonDTOView>> doSearchByFirstName(@Valid @PathVariable String firstName) {
    List<PersonDTOView> responseBody = personService.searchByFirstName(firstName);
    return ResponseEntity.ok(responseBody);
  }

  // SEARCH BY LAST NAME
  @GetMapping("/search/last-name/{lastName}")
  public ResponseEntity<List<PersonDTOView>> doSearchByLastName(@Valid @PathVariable String lastName) {
    List<PersonDTOView> responseBody = personService.searchByLastName(lastName);
    return ResponseEntity.ok(responseBody);
  }

  // FIND IDLE PEOPLE (no tasks)
  @GetMapping("/idle")
  public ResponseEntity<List<PersonDTOView>> doFindIdlePeople() {
    List<PersonDTOView> responseBody = personService.findIdlePeople();
    return ResponseEntity.ok(responseBody);
  }
}
