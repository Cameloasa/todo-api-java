package dev.cameloasa.todoapi.converter;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskConverterImpl implements TaskConverter {

    @Override
    public TaskDTOView toTaskDTOView(Task entity) {
        if (entity == null) return null;

        return TaskDTOView.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .deadline(entity.getDeadline())
                .done(entity.isDone())
                .build();
    }

    @Override
    public Task toTaskEntity(TaskDTOForm dtoForm) {
        if (dtoForm == null) return null;

        return Task.builder()
                .id(dtoForm.getId())
                .title(dtoForm.getTitle())
                .description(dtoForm.getDescription())
                .deadline(dtoForm.getDeadline())
                .done(dtoForm.isDone())
                .build();
    }
}