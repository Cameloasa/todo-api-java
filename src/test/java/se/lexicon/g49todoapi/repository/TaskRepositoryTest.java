package se.lexicon.g49todoapi.repository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.Task;
import dev.cameloasa.todoapi.repository.PersonRepository;
import dev.cameloasa.todoapi.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PersonRepository personRepository;

    private Person existingPerson;
    private Task existingTask1;
    private Task existingTask2;


    @BeforeEach
    void setUp() {
        Person existingPerson = personRepository.save(new Person("Anna Banana"));
        Task existingTask1 = taskRepository.save(new Task("Test Todo 1","task description Todo 1", LocalDate.now().plusDays(10),existingPerson));
        Task existingTask2 = taskRepository.save(new Task("Test Todo 2","task description Todo 2", LocalDate.now().plusDays(14),existingPerson));
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
