package dev.cameloasa.todoapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.exception.InvalidCredentialsException;
import dev.cameloasa.todoapi.service.PersonService;
import dev.cameloasa.todoapi.service.SessionService;
import dev.cameloasa.todoapi.service.TaskService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth/tasks/my")
public class MyTaskController {

    private final TaskService taskService;
    private final SessionService sessionService;
    private final PersonService personService;

    public MyTaskController(TaskService taskService,
                            SessionService sessionService,
                            PersonService personService) {
        this.taskService = taskService;
        this.sessionService = sessionService;
        this.personService = personService;
    }

    // ---------------------------------------------------------
    // Helper: get current personId from session token
    // ---------------------------------------------------------
    private Long getCurrentPersonId(String sessionToken) {
        String email = sessionService.getUserEmail(sessionToken);
        PersonDTOView person = personService.findByUserEmail(email);
        return person.getId();
    }

    // ---------------------------------------------------------
    // GET all my tasks
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<TaskDTOView>> getMyTasks(
            @RequestHeader("X-Session-Token") String token) {

        Long personId = getCurrentPersonId(token);
        return ResponseEntity.ok(taskService.findByPersonId(personId));
    }

    // ---------------------------------------------------------
    // GET my task by ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTOView> getMyTaskById(
            @RequestHeader("X-Session-Token") String token,
            @PathVariable Long id) {

        Long personId = getCurrentPersonId(token);
        TaskDTOView task = taskService.findById(id);

        if (!task.getPersonId().equals(personId)) {
            throw new InvalidCredentialsException("Not your task");
        }

        return ResponseEntity.ok(task);
    }

    // ---------------------------------------------------------
    // CREATE my task
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<TaskDTOView> createMyTask(
            @RequestHeader("X-Session-Token") String token,
            @RequestBody TaskDTOForm dtoForm) {

        Long personId = getCurrentPersonId(token);
        dtoForm.setPersonId(personId); // user creează DOAR pentru el

        TaskDTOView created = taskService.create(dtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------------------------------------------------
    // UPDATE my task
    // ---------------------------------------------------------
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTOView> updateMyTask(
            @RequestHeader("X-Session-Token") String token,
            @PathVariable Long id,
            @RequestBody TaskDTOForm dtoForm) {

        Long personId = getCurrentPersonId(token);
        TaskDTOView existing = taskService.findById(id);

        if (!existing.getPersonId().equals(personId)) {
            throw new InvalidCredentialsException("Not your task");
        }

        dtoForm.setId(id);
        dtoForm.setPersonId(personId);

        return ResponseEntity.ok(taskService.update(dtoForm));
    }

    // ---------------------------------------------------------
    // DELETE my task
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyTask(
            @RequestHeader("X-Session-Token") String token,
            @PathVariable Long id) {

        Long personId = getCurrentPersonId(token);
        TaskDTOView existing = taskService.findById(id);

        if (!existing.getPersonId().equals(personId)) {
            throw new InvalidCredentialsException("Not your task");
        }

        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------
    // MARK DONE
    // ---------------------------------------------------------
    @PutMapping("/{id}/done")
    public ResponseEntity<TaskDTOView> markDone(
            @RequestHeader("X-Session-Token") String token,
            @PathVariable Long id) {

        Long personId = getCurrentPersonId(token);
        TaskDTOView task = taskService.findById(id);

        if (!task.getPersonId().equals(personId)) {
            throw new InvalidCredentialsException("Not your task");
        }

        return ResponseEntity.ok(taskService.markDone(id));
    }

    // ---------------------------------------------------------
    // MARK UNDONE
    // ---------------------------------------------------------
    @PutMapping("/{id}/undone")
    public ResponseEntity<TaskDTOView> markUndone(
            @RequestHeader("X-Session-Token") String token,
            @PathVariable Long id) {

        Long personId = getCurrentPersonId(token);
        TaskDTOView task = taskService.findById(id);

        if (!task.getPersonId().equals(personId)) {
            throw new InvalidCredentialsException("Not your task");
        }

        return ResponseEntity.ok(taskService.markUndone(id));
    }
}

