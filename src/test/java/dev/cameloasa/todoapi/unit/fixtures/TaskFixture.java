package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Task;

public class TaskFixture {

  public static Task sampleTask() {
    Task task = new Task();
    task.setId(10L);
    task.setTitle("Test Task");
    task.setDescription("Fixture task description");
    task.setDone(false);
    return task;
  }


    public static TaskDTOView sampleTaskDTOView() {
        return TaskDTOView.builder()
                .id(10L)
                .title("Test Task")
                .description("Fixture task description")
                .done(false)
                .build();
    }

    public static TaskDTOForm sampleTaskDTOForm() {
        TaskDTOForm dto = new TaskDTOForm();
        dto.setId(10L);
        dto.setTitle("Test Task");
        dto.setDescription("Fixture task description");
        dto.setDone(false);
        return dto;
    }

  }

