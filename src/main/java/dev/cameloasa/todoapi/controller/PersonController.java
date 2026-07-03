package dev.cameloasa.todoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.cameloasa.todoapi.domanin.dto.*;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.service.PersonService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend URL
@RequestMapping("api/v1/persons")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonDTOView> doCreate(@RequestBody PersonDTOForm dtoForm) {
        System.out.println("DTO Form: " + dtoForm);
        PersonDTOView responseBody = personService.create(dtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTOView> doFindPersonById(@PathVariable Long id) {
        System.out.println(">>>>>>> get Person by Id has been executed");
        System.out.println("Id: " + id);

        PersonDTOView responseBody = personService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<PersonDTOView>>doFindAllRoles(){
        List<PersonDTOView> responseBody = personService.findAll();
        return ResponseEntity.ok(responseBody);

    }
    @PutMapping("/update")
    public ResponseEntity<Void> doUpdate(@RequestBody PersonDTOForm dtoForm) {
        System.out.println(">>>>>>> Updated Person By DTO form has been executed");
        personService.update(dtoForm);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> doDelete(@PathVariable Long id) {
        System.out.println(">>>>>>> Deleted Person By Id has been executed");
        personService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
