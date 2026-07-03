package dev.cameloasa.todoapi.converter;

import org.springframework.stereotype.Component;

import dev.cameloasa.todoapi.domanin.dto.TaskDTOForm;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.domanin.entity.Task;

@Component
public class TaskConverterImpl implements TaskConverter {

    @Override
    public TaskDTOView toTaskDTOView(Task entity) {
        return TaskDTOView.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .person(entity.getPerson())
                .Deadline(entity.getDeadline())
                .done(entity.isDone())
                .build();
    }

    @Override
    public Task toTaskEntity(TaskDTOForm dtoForm) {
        return Task.builder()
                .id(dtoForm.getId())
                .title(dtoForm.getTitle())
                .description(dtoForm.getDescription())
                .person(dtoForm.getPerson())
                .deadline(dtoForm.getDeadline())
                .done(dtoForm.getDone())
                .build();
    }
}
