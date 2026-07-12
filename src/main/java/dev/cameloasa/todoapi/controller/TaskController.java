package dev.cameloasa.todoapi.controller;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.service.TaskService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth/tasks")
@RestController
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  // CREATE
  @PostMapping
  public ResponseEntity<TaskDTOView> doCreate(@RequestBody TaskDTOForm dtoForm) {
    TaskDTOView responseBody = taskService.create(dtoForm);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
  }

  // FIND BY ID
  @GetMapping("/{id}")
  public ResponseEntity<TaskDTOView> doFindTaskById(@PathVariable Long id) {
    TaskDTOView responseBody = taskService.findById(id);
    return ResponseEntity.ok(responseBody);
  }

  // FIND ALL
  @GetMapping
  public ResponseEntity<List<TaskDTOView>> doFindAll() {
    List<TaskDTOView> responseBody = taskService.findAll();
    return ResponseEntity.ok(responseBody);
  }

  // UPDATE
  @PatchMapping
  public ResponseEntity<TaskDTOView> doUpdate(@RequestBody TaskDTOForm dtoForm) {
    TaskDTOView updated = taskService.update(dtoForm);
    return ResponseEntity.ok(updated);
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> doDelete(@PathVariable Long id) {
    taskService.delete(id);
    return ResponseEntity.noContent().build();
  }

  // FIND BY PERSON ID
  @GetMapping("/person/{personId}")
  public ResponseEntity<List<TaskDTOView>> doFindByPersonId(@PathVariable Long personId) {
    List<TaskDTOView> responseBody = taskService.findByPersonId(personId);
    return ResponseEntity.ok(responseBody);
  }

  // FIND BY DEADLINE RANGE
  @GetMapping("/deadline")
  public ResponseEntity<List<TaskDTOView>> doFindByDeadlineBetween(
      @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

    List<TaskDTOView> responseBody = taskService.findByDeadlineBetween(startDate, endDate);
    return ResponseEntity.ok(responseBody);
  }

  // FIND TASKS WITHOUT PERSON
  @GetMapping("/unassigned")
  public ResponseEntity<List<TaskDTOView>> doFindByPersonIsNull() {
    List<TaskDTOView> responseBody = taskService.findByPersonIsNull();
    return ResponseEntity.ok(responseBody);
  }

  // FIND UNFINISHED + OVERDUE
  @GetMapping("/overdue")
  public ResponseEntity<List<TaskDTOView>> doFindUnfinishedAndOverdue() {
    List<TaskDTOView> responseBody = taskService.findUnfinishedAndOverdueTasks();
    return ResponseEntity.ok(responseBody);
  }

  // SEARCH BY TITLE
  @GetMapping("/search/title")
  public ResponseEntity<List<TaskDTOView>> doFindByTitleContaining(@RequestParam String title) {
    List<TaskDTOView> responseBody = taskService.findByTitleContaining(title);
    return ResponseEntity.ok(responseBody);
  }

  // SEARCH BY DESCRIPTION
  @GetMapping("/search/description")
  public ResponseEntity<List<TaskDTOView>> doFindByDescriptionContaining(
      @RequestParam String description) {
    List<TaskDTOView> responseBody = taskService.findByDescriptionContaining(description);
    return ResponseEntity.ok(responseBody);
  }

  // TASKS DUE TODAY
  @GetMapping("/due-today")
  public ResponseEntity<List<TaskDTOView>> doFindTasksDueToday() {
    List<TaskDTOView> responseBody = taskService.findTasksDueToday();
    return ResponseEntity.ok(responseBody);
  }

  // UPCOMING TASKS
  @GetMapping("/upcoming")
  public ResponseEntity<List<TaskDTOView>> doFindUpcomingTasks() {
    List<TaskDTOView> responseBody = taskService.findUpcomingTasks();
    return ResponseEntity.ok(responseBody);
  }

  // MARK DONE
  @PutMapping("/{id}/done")
  public ResponseEntity<TaskDTOView> doMarkDone(@PathVariable Long id) {
    TaskDTOView responseBody = taskService.markDone(id);
    return ResponseEntity.ok(responseBody);
  }

  // MARK UNDONE
  @PutMapping("/{id}/undone")
  public ResponseEntity<TaskDTOView> doMarkUndone(@PathVariable Long id) {
    TaskDTOView responseBody = taskService.markUndone(id);
    return ResponseEntity.ok(responseBody);
  }

  // REMOVE TASK FROM PERSON
  @PutMapping("/{taskId}/remove-person")
  public ResponseEntity<TaskDTOView> doRemoveTaskFromPerson(@PathVariable Long taskId) {
    TaskDTOView responseBody = taskService.removeTaskFromPerson(taskId);
    return ResponseEntity.ok(responseBody);
  }

  // REASSIGN TASK TO PERSON
  @PutMapping("/{taskId}/reassign")
  public ResponseEntity<TaskDTOView> doReassignTaskToPerson(
      @PathVariable Long taskId, @RequestParam Long newPersonId) {

    TaskDTOView responseBody = taskService.reassignTaskToPerson(taskId, newPersonId);
    return ResponseEntity.ok(responseBody);
  }
}
