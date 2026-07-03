package dev.cameloasa.todoapi.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Task;

public interface TaskService {
    // create
    TaskDTOView create(TaskDTOForm dtoForm);
    // findById
    TaskDTOView findById(Long id);
    // update
    TaskDTOView update(TaskDTOForm dtoForm);
    // delete
    void delete(Long id);
    // findTasksByPersonId
    List<TaskDTOView> findByPersonId(Long personId);
    // findTasksBetweenStartAndEndDate
    List<TaskDTOView> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);
    // findAllUnassignedTasks
    List<TaskDTOView> findByPersonIsNull();
    //findAllUnfinishedTasksAndOverdue
    List<TaskDTOView> findUnfinishedAndOverdueTasks();
    TaskDTOView addTaskToPerson(Long personId, TaskDTOForm taskDTOForm);
}
