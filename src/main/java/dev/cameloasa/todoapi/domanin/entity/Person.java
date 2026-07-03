package dev.cameloasa.todoapi.domanin.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"tasks", "user"})
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true)
  private String username;

  @OneToOne
  @JoinColumn(name = "user_email", unique = true)
  private User user;

  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Task> tasks = new ArrayList<>();

  public Person(String firstName, String lastName, String username) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
  }

  public void addTask(Task... tasks) {
    if (Objects.requireNonNull(tasks).length == 0)
      throw new IllegalArgumentException("Tasks must have at least one task");

    for (Task task : tasks) {
      this.tasks.add(task);
      if (task.getPerson() != this) {
        task.setPerson(this);
      }
    }
  }

  public void removeTask(Task... tasks) {
    if (Objects.requireNonNull(tasks).length == 0)
      throw new IllegalArgumentException("Tasks is empty");

    for (Task task : tasks) {
      if (this.tasks.remove(task) && task.getPerson() == this) {
        task.setPerson(null);
      }
    }
  }
}
