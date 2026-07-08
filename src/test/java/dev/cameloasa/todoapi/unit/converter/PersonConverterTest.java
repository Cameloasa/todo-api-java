package dev.cameloasa.todoapi.unit.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.cameloasa.todoapi.converter.PersonConverter;
import dev.cameloasa.todoapi.converter.PersonConverterImpl;
import dev.cameloasa.todoapi.converter.TaskConverter;
import dev.cameloasa.todoapi.unit.fixtures.PersonFixture;
import dev.cameloasa.todoapi.unit.fixtures.TaskFixture;
import dev.cameloasa.todoapi.unit.fixtures.UserFixture;

class PersonConverterTest {

    @Mock
    private TaskConverter taskConverter;

    private PersonConverter converter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        converter = new PersonConverterImpl(taskConverter);
    }

    @Test
    void toPersonDTOView_should_map_tasks() {
        var user = UserFixture.sampleUser();
        var person = PersonFixture.samplePerson(user);

        var task = TaskFixture.sampleTask();
        person.setTasks(List.of(task));

        var taskDTO = TaskFixture.sampleTaskDTOView();

        when(taskConverter.toTaskDTOView(task)).thenReturn(taskDTO);

        var dto = converter.toPersonDTOView(person);

        assertNotNull(dto.getTasks());
        assertEquals(1, dto.getTasks().size());
        assertEquals(taskDTO.getTitle(), dto.getTasks().get(0).getTitle());
    }
}
