package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Task;

public interface TaskConverter {

    TaskDTOView toTaskDTOView(Task entity);
    Task toTaskEntity(TaskDTOForm dtoForm);
}
