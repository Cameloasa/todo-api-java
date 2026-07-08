package dev.cameloasa.todoapi.unit.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.cameloasa.todoapi.converter.TaskConverter;
import dev.cameloasa.todoapi.converter.TaskConverterImpl;
import dev.cameloasa.todoapi.unit.fixtures.TaskFixture;
import org.junit.jupiter.api.Test;

class TaskConverterTest {

  private final TaskConverter converter = new TaskConverterImpl();

  @Test
  void toTaskDTOView_should_return_null_when_entity_is_null() {
    assertNull(converter.toTaskDTOView(null));
  }

  @Test
  void toTaskDTOView_should_map_fields_correctly() {
    var entity = TaskFixture.sampleTask();

    var dto = converter.toTaskDTOView(entity);

    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getTitle(), dto.getTitle());
    assertEquals(entity.getDescription(), dto.getDescription());
    assertEquals(entity.getDeadline(), dto.getDeadline());
    assertEquals(entity.isDone(), dto.isDone());
  }

  @Test
  void toTaskEntity_should_return_null_when_dto_is_null() {
    assertNull(converter.toTaskEntity(null));
  }

  @Test
  void toTaskEntity_should_map_fields_correctly() {
    var dto = TaskFixture.sampleTaskDTOForm();

    var entity = converter.toTaskEntity(dto);

    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getTitle(), entity.getTitle());
    assertEquals(dto.getDescription(), entity.getDescription());
    assertEquals(dto.getDeadline(), entity.getDeadline());
    assertEquals(dto.isDone(), entity.isDone());
  }
}
