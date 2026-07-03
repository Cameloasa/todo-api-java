package dev.cameloasa.todoapi.domanin.dto;

import lombok.*;

import java.util.List;

import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.domanin.entity.User;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class PersonDTOView {
    private Long id;
    private String name;

}
