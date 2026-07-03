package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskDTOView create(TaskDTOForm dtoForm);
    TaskDTOView findById(Long id);
    TaskDTOView update(TaskDTOForm dtoForm);
    void delete(Long id);

    List<TaskDTOView> findAll();

    List<TaskDTOView> findByPersonId(Long personId);
    List<TaskDTOView> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);
    List<TaskDTOView> findByPersonIsNull();
    List<TaskDTOView> findUnfinishedAndOverdueTasks();

    List<TaskDTOView> findByTitleContaining(String title);
    List<TaskDTOView> findByDescriptionContaining(String description);

    List<TaskDTOView> findTasksDueToday();
    List<TaskDTOView> findUpcomingTasks();

    TaskDTOView markDone(Long id);
    TaskDTOView markUndone(Long id);

    TaskDTOView removeTaskFromPerson(Long taskId);
    TaskDTOView reassignTaskToPerson(Long taskId, Long newPersonId);

    TaskDTOView addTaskToPerson(Long personId, TaskDTOForm taskDTOForm);
}
