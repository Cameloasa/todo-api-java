package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;

public class PersonFixture {

  public static Person samplePerson(User user) {
    Person person = new Person();
    person.setFirstName("Test");
    person.setLastName("Person");
    person.setUser(user);
    return person;
  }
}
