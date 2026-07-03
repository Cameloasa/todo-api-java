package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.converter.TaskConverter;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskConverter taskConverter;
  private final PersonRepository personRepository;

  public TaskServiceImpl(
      TaskRepository taskRepository,
      TaskConverter taskConverter,
      PersonRepository personRepository) {
    this.taskRepository = taskRepository;
    this.taskConverter = taskConverter;
    this.personRepository = personRepository;
  }

  @Override
  @Transactional
  public TaskDTOView create(TaskDTOForm dtoForm) {
    if (dtoForm == null) throw new IllegalArgumentException("TaskDTOForm is null");

    Task task = taskConverter.toTaskEntity(dtoForm);

    if (dtoForm.getPersonId() != null) {
      Person person =
          personRepository
              .findById(dtoForm.getPersonId())
              .orElseThrow(() -> new DataNotFoundException("Person not found"));
      task.setPerson(person);
    }

    Task savedTask = taskRepository.save(task);
    return taskConverter.toTaskDTOView(savedTask);
  }

  @Override
  public TaskDTOView findById(Long id) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Task not found"));
    return taskConverter.toTaskDTOView(task);
  }

  @Override
  public List<TaskDTOView> findAll() {
    return taskRepository.findAll().stream().map(taskConverter::toTaskDTOView).toList();
  }

  @Override
  @Transactional
  public TaskDTOView update(TaskDTOForm dtoForm) {
    if (dtoForm == null) throw new IllegalArgumentException("TaskDTOForm is null");

    Task existingTask =
        taskRepository
            .findById(dtoForm.getId())
            .orElseThrow(() -> new DataNotFoundException("Task not found"));

    existingTask.setTitle(dtoForm.getTitle());
    existingTask.setDescription(dtoForm.getDescription());
    existingTask.setDeadline(dtoForm.getDeadline());
    existingTask.setDone(dtoForm.isDone());

    if (dtoForm.getPersonId() != null) {
      Person person =
          personRepository
              .findById(dtoForm.getPersonId())
              .orElseThrow(() -> new DataNotFoundException("Person not found"));
      existingTask.setPerson(person);
    }

    Task updatedTask = taskRepository.save(existingTask);
    return taskConverter.toTaskDTOView(updatedTask);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (!taskRepository.existsById(id)) throw new DataNotFoundException("Task not found");

    taskRepository.deleteById(id);
  }

  @Override
  public List<TaskDTOView> findByPersonId(Long personId) {
    return taskRepository.findByPersonId(personId).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  public List<TaskDTOView> findByDeadlineBetween(LocalDate startDate, LocalDate endDate) {
    return taskRepository.findByDeadlineBetween(startDate, endDate).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  public List<TaskDTOView> findByPersonIsNull() {
    return taskRepository.findByPersonIsNull().stream().map(taskConverter::toTaskDTOView).toList();
  }

  @Override
  public List<TaskDTOView> findUnfinishedAndOverdueTasks() {
    return taskRepository.findUnfinishedAndOverdueTasks(LocalDate.now()).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  public List<TaskDTOView> findByTitleContaining(String title) {
    return taskRepository.findByTitleContaining(title).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  public List<TaskDTOView> findByDescriptionContaining(String description) {
    return taskRepository.findByDescriptionContaining(description).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  public List<TaskDTOView> findTasksDueToday() {
    return taskRepository.findByDeadline(LocalDate.now()).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  public List<TaskDTOView> findUpcomingTasks() {
    return taskRepository.findByDeadlineAfter(LocalDate.now()).stream()
        .map(taskConverter::toTaskDTOView)
        .toList();
  }

  @Override
  @Transactional
  public TaskDTOView markDone(Long id) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Task not found"));

    task.setDone(true);
    taskRepository.save(task);

    return taskConverter.toTaskDTOView(task);
  }

  @Override
  @Transactional
  public TaskDTOView markUndone(Long id) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Task not found"));

    task.setDone(false);
    taskRepository.save(task);

    return taskConverter.toTaskDTOView(task);
  }

  @Override
  @Transactional
  public TaskDTOView removeTaskFromPerson(Long taskId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new DataNotFoundException("Task not found"));

    task.setPerson(null);
    taskRepository.save(task);

    return taskConverter.toTaskDTOView(task);
  }

  @Override
  @Transactional
  public TaskDTOView reassignTaskToPerson(Long taskId, Long newPersonId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new DataNotFoundException("Task not found"));

    Person person =
        personRepository
            .findById(newPersonId)
            .orElseThrow(() -> new DataNotFoundException("Person not found"));

    task.setPerson(person);
    taskRepository.save(task);

    return taskConverter.toTaskDTOView(task);
  }
}
