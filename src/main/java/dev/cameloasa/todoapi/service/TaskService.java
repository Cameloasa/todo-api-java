package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {

  // create, read, update, delete
  TaskDTOView create(TaskDTOForm dtoForm);

  TaskDTOView findById(Long id);

  TaskDTOView update(TaskDTOForm dtoForm);

  void delete(Long id);

  // find all
  List<TaskDTOView> findAll();

  // find by various criteria
  List<TaskDTOView> findByPersonId(Long personId);

  List<TaskDTOView> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);

  List<TaskDTOView> findByPersonIsNull();

  List<TaskDTOView> findUnfinishedAndOverdueTasks();

  // additional methods for searching and managing tasks
  List<TaskDTOView> findByTitleContaining(String title);

  List<TaskDTOView> findByDescriptionContaining(String description);

  // methods for finding tasks based on deadlines
  List<TaskDTOView> findTasksDueToday();

  List<TaskDTOView> findUpcomingTasks();

  // methods for marking tasks as done or undone
  TaskDTOView markDone(Long id);

  TaskDTOView markUndone(Long id);

  // methods for managing task assignments to persons
  TaskDTOView removeTaskFromPerson(Long taskId);

  TaskDTOView reassignTaskToPerson(Long taskId, Long newPersonId);
}
