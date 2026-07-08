package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.entity.Task;

public class TaskFixture {

  public static Task sampleTask() {
    Task task = new Task();
    task.setTitle("Test Task");
    task.setDescription("Fixture task description");
    task.setDone(false);
    return task;
  }
}
