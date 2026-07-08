package dev.cameloasa.todoapi.unit.fixtures;

import java.util.List;

import dev.cameloasa.todoapi.domanin.dto.PersonDTOForm;
import dev.cameloasa.todoapi.domanin.dto.PersonDTOView;
import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.domanin.entity.User;

public class PersonFixture {

  public static Person samplePerson(User user) {
    Person person = new Person();
    person.setId(1L);
    person.setFirstName("Test");
    person.setLastName("Person");
    person.setUser(user);
    return person;
  }

  public static PersonDTOForm samplePersonDTOForm() {
        PersonDTOForm dto = new PersonDTOForm();
        dto.setId(1L);
        dto.setFirstName("Test");
        dto.setLastName("Person");
        return dto;
    }

    public static PersonDTOView samplePersonDTOView(User user) {
        return PersonDTOView.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Person")
                .userEmail(user.getEmail())
                .tasks(null) // sau List.of(...) dacă vrei
                .build();
    }

    public static Person samplePersonWithTasks(User user, List<Task> tasks) {
        Person person = samplePerson(user);
        person.setTasks(tasks);
        return person;
    }
}
