package se.lexicon.g49todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PersonRepositoryTest {
  @Autowired private PersonRepository personRepository;
  private Person person;

  @BeforeEach
  public void setUp() {
    person = Person.builder().name("John Doe").build();
    personRepository.save(person);
  }
  /*@Transactional
  @Test
  void testFindIdlePeople() {
      List<Person> idlePeople = personRepository.findIdlePeople();
      assertFalse(idlePeople.isEmpty());
      assertEquals(1, idlePeople.size());
      assertEquals("John Doe", idlePeople.get(0).getName());
  }*/
}
