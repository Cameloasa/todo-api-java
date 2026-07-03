package dev.cameloasa.todoapi.domanin.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "person")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private String title;

  private String description;

  private LocalDate deadline;

  private boolean done;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id", nullable = false) // task-ul has allways owner
  private Person person;

  // Constructor complet
  public Task(String title, String description, LocalDate deadline, boolean done, Person person) {
    this.title = title;
    this.description = description;
    this.deadline = deadline;
    this.done = done;
    this.person = person;
  }

  // Constructor without done (implicit false)
  public Task(String title, String description, LocalDate deadline, Person person) {
    this.title = title;
    this.description = description;
    this.deadline = deadline;
    this.person = person;
  }

  // Constructor without person (for DTO-uri)
  public Task(String title, String description, LocalDate deadline) {
    this.title = title;
    this.description = description;
    this.deadline = deadline;
  }
}
