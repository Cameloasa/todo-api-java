package dev.cameloasa.todoapi.converter;

import org.springframework.stereotype.Component;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;

import java.util.stream.Collectors;

@Component
public class PersonConverterImpl implements PersonConverter {

    @Override
    public PersonDTOView toPersonDTOView(Person entity) {
        return PersonDTOView.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public Person toPersonEntity(PersonDTOForm dtoForm) {
        return Person.builder()
                .id(dtoForm.getId())
                .name(dtoForm.getName())
                .build();
    }


}

