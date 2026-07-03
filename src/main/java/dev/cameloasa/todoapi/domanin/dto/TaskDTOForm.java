package dev.cameloasa.todoapi.domanin.dto;

import lombok.*;

import java.time.LocalDate;

import dev.cameloasa.todoapi.domanin.entity.Person;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskDTOForm {

    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Boolean done;
    private Person person;
}
