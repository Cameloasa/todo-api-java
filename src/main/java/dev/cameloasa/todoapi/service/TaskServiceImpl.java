package dev.cameloasa.todoapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.TaskConverter;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.exception.DataDuplicateException;
import dev.cameloasa.todoapi.exception.DataNotFoundException;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private TaskConverter taskConverter;
    private PersonRepository personRepository;
    private PersonConverter personConverter;

    public TaskServiceImpl(TaskRepository taskRepository, TaskConverter taskConverter, PersonRepository personRepository, PersonConverter personConverter) {
        this.taskRepository = taskRepository;
        this.taskConverter = taskConverter;
        this.personRepository = personRepository;
        this.personConverter = personConverter;
    }

    @Override
    @Transactional
    public TaskDTOView create(TaskDTOForm dtoForm) {
        //Check if task exist in database
        if (dtoForm == null) throw new IllegalArgumentException("TaskDTOForm is null");
        //Check if the task exist in the database, if exist already throw a data duplicate exception
        boolean taskExists = taskRepository.existsById(dtoForm.getId());
        if (taskExists)
            throw new DataDuplicateException("Task already exists");
        //Convert TaskDTOForm to TaskEntity using converter
        Task task = taskConverter.toTaskEntity(dtoForm);

        // Check if the person exists and set it to the task
        if (dtoForm.getPerson() != null) {
            Long personId = dtoForm.getPerson().getId();
            Person person = personRepository.findById(personId)
                    .orElseThrow(() -> new DataNotFoundException("Person not found with id: " + personId));
            task.setPerson(person);
        }
        //Save task to the database
        Task savedTask = taskRepository.save(task);
        //Convert savedTask to TaskDTOView and return
        return taskConverter.toTaskDTOView(savedTask);
    }

    @Override
    public TaskDTOView findById(Long id) {
        //1.Find the task by id in repository else throw exception
        if (id == null) throw new IllegalArgumentException("TaskDTOForm is null");
        Task task = taskRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Task not found"));
        //2.Convert to DTO view
        return taskConverter.toTaskDTOView(task);
    }

    @Override
    @Transactional
    public TaskDTOView update(TaskDTOForm dtoForm) {
        // Check the param
        if (dtoForm == null) throw new IllegalArgumentException("This Form is not accepted.");
        // Find the existing task
        Task existingTask = taskRepository.findById(dtoForm.getId()).
                orElseThrow(() -> new DataNotFoundException("The task does not exist."));
        // Update the task details
        existingTask.setTitle(dtoForm.getTitle());
        existingTask.setDescription(dtoForm.getDescription());
        existingTask.setDeadline(dtoForm.getDeadline());
        existingTask.setDone(dtoForm.getDone());

        // Check if the person exists and set it to the task
        if (dtoForm.getPerson() != null) {
            Long personId = dtoForm.getPerson().getId();
            Person person = personRepository.findById(personId)
                    .orElseThrow(() -> new DataNotFoundException("Person not found with id: " + personId));
            existingTask.setPerson(person);
        }
       //Save the task
        Task updatedTask = taskRepository.save(existingTask);
        // Convert to DTO view and return
        return taskConverter.toTaskDTOView(updatedTask);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        //Check if task exist by id in repository
        if (!taskRepository.existsById(id)) throw new DataNotFoundException("TaskDTOForm does not exist.");
        //delete task from repository
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTOView> findByPersonId(Long personId) {
        List<Task> tasks = taskRepository.findByPersonId(personId);

        return tasks.stream().map(taskConverter::toTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findByDeadlineBetween(LocalDate startDate, LocalDate endDate) {
        List<Task> tasks = taskRepository.findByDeadlineBetween(startDate, endDate);

        return tasks.stream().map(taskConverter::toTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findByPersonIsNull() {
        List<Task> tasks = taskRepository.findByPersonIsNull();
        return tasks.stream().map(taskConverter::toTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findUnfinishedAndOverdueTasks() {
        List<Task> tasks = taskRepository.findUnfinishedAndOverdueTasks(LocalDate.now());
        return tasks.stream().map(taskConverter::toTaskDTOView).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDTOView addTaskToPerson(Long personId, TaskDTOForm taskDTOForm) {
        Person person = personRepository.findById(personId).orElseThrow(() -> new DataNotFoundException("Person not found"));
        Task task = taskConverter.toTaskEntity(taskDTOForm);
        task.setPerson(person);
        taskRepository.save(task);
        return taskConverter.toTaskDTOView(task);

    }

}
