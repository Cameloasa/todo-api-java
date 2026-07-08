package dev.cameloasa.todoapi.integration.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import dev.cameloasa.todoapi.domanin.entity.Person;
import dev.cameloasa.todoapi.domanin.entity.User;
import dev.cameloasa.todoapi.domanin.entity.Task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")

public class TaskApiIntegrationTest extends IntegrationTestBase{

    @Autowired private MockMvc mockMvc;

    // -------------------------
  // Positive tests
  // -------------------------

   // -------------------------
  // Create task
  // -------------------------
  @SuppressWarnings("null")
  @Test
void testCreateTask() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");

    String json = """
        {
            "title": "My Task",
            "description": "Do something important",
            "deadline": "2026-07-10",
            "done": false,
            "personId": %d
        }
        """.formatted(person.getId());

    mockMvc.perform(
            post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("My Task"))
        .andExpect(jsonPath("$.description").value("Do something important"))
        .andExpect(jsonPath("$.deadline").value("2026-07-10"))
        .andExpect(jsonPath("$.done").value(false));
}

 // -------------------------
  // Find task by id
  // -------------------------
@Test
void testFindTaskById() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");
    Task task = createTask(person, "My Task", "Do something");

    mockMvc.perform(get("/tasks/" + task.getId()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(task.getId()))
        .andExpect(jsonPath("$.title").value("My Task"))
        .andExpect(jsonPath("$.description").value("Do something"));
}

// -------------------------
  // Find all task 
  // -------------------------
@Test
void testFindAllTasks() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");

    createTask(person, "Task A", "Desc A");
    createTask(person, "Task B", "Desc B");

    mockMvc.perform(get("/tasks"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
}

// -------------------------
  // Update task 
  // -------------------------
@SuppressWarnings("null")
@Test
void testUpdateTask() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");
    Task task = createTask(person, "Old Title", "Old Desc");

    String json = """
        {
            "id": %d,
            "title": "New Title",
            "description": "New Desc",
            "deadline": "2026-07-10",
            "done": true,
            "personId": %d
        }
        """.formatted(task.getId(), person.getId());

    mockMvc.perform(
            patch("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("New Title"))
        .andExpect(jsonPath("$.done").value(true));
}

// -------------------------
  // Delete task 
  // -------------------------
@Test
void testDeleteTask() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");
    Task task = createTask(person, "To Delete", "Desc");

    mockMvc.perform(delete("/tasks/" + task.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());
}

// -------------------------
  // Find by person id
  // -------------------------
@Test
void testFindByPersonId() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");

    createTask(person, "Task A", "Desc A");
    createTask(person, "Task B", "Desc B");

    mockMvc.perform(get("/tasks/person/" + person.getId()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
}

// -------------------------
  // Mark done
  // -------------------------
@Test
void testMarkTaskDone() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");
    Task task = createTask(person, "My Task", "Desc");

    mockMvc.perform(put("/tasks/" + task.getId() + "/done"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.done").value(true));
}

// -------------------------
  // Mark undone
  // -------------------
@Test
void testMarkTaskUndone() throws Exception {

    User user = createUser("test@example.com");
    Person person = createPerson(user, "Test", "Person");
    Task task = createTask(person, "My Task", "Desc");
    task.setDone(true);
    taskRepository.save(task);

    mockMvc.perform(put("/tasks/" + task.getId() + "/undone"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.done").value(false));
}

// -------------------------
  // Reassign task
  // -------------------
@Test
void testReassignTaskToPerson() throws Exception {

    User user1 = createUser("test@example.com");
    User user2 = createUser("user2@example.com");

    Person p1 = createPerson(user1, "Test", "Person1");
    Person p2 = createPerson(user2, "Test", "Person2");

    Task task = createTask(p1, "My Task", "Desc");

    mockMvc.perform(
            put("/tasks/" + task.getId() + "/reassign")
                .param("newPersonId", p2.getId().toString()))
        .andDo(print())
        .andExpect(status().isOk());
}

// -------------------------
  // Remove person from task
  // -------------------
@Test
void testRemoveTaskFromPerson() throws Exception {

    User user = createUser("remove@example.com");
    Person person = createPerson(user, "Test", "Person");
    Task task = createTask(person, "My Task", "Desc");

    mockMvc.perform(put("/tasks/" + task.getId() + "/remove-person"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.person").doesNotExist());
}

}
