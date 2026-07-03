package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TaskRepositoryTest {
  @Autowired private TaskRepository taskRepository;
  @Autowired private PersonRepository personRepository;

  private Person existingPerson;
  private Task existingTask1;
  private Task existingTask2;

  @BeforeEach
  void setUp() {
    Person person = Person.builder().firstName("John").lastName("Smith").build();
    Task existingTask1 =
        taskRepository.save(
            new Task(
                "Test Todo 1",
                "task description Todo 1",
                LocalDate.now().plusDays(10),
                existingPerson));
    Task existingTask2 =
        taskRepository.save(
            new Task(
                "Test Todo 2",
                "task description Todo 2",
                LocalDate.now().plusDays(14),
                existingPerson));
  }
  /*@Test
  @Transactional
  public void testFindByTitleContaining() {
      String titleContaining = "test";
      List<Task> expected = new ArrayList<>(Arrays.asList(existingTask1, existingTask2));
      List<Task> actual = taskRepository.findByTitleContaining(titleContaining);

      assertNotNull(expected);
      assertNotNull(actual);
      assertEquals(expected, actual);
  }

  @Test
  @Transactional
  public void testFindByPerson_Id() {
      Long personId = existingPerson.getId();
      List<Task> expected = new ArrayList<>(Arrays.asList(existingTask1, existingTask2));
      List<Task> actual = taskRepository.findByPersonId(personId);

      assertNotNull(expected);
      assertNotNull(actual);
      assertEquals(expected, actual);
  }*/
}
