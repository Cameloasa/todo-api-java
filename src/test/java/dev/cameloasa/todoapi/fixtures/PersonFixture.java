package dev.cameloasa.todoapi.fixtures;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;

public class PersonFixture {

  public static Person samplePerson(User user) {
    Person person = new Person();
    person.setFirstName("Camelia");
    person.setLastName("Test");
    person.setUser(user);
    return person;
  }
}
