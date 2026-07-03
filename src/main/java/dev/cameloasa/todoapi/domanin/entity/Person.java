package dev.cameloasa.todoapi.domanin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tasks","user"})
@ToString
@Builder

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "person")
    private List<Task> tasks = new ArrayList<Task>();

    public Person(String name) {
        this.name = name;
    }

    public void addTask(Task... tasks) {
        if (Objects.requireNonNull(tasks).length == 0)
            throw new IllegalArgumentException("Tasks must have at least one task");
        for (Task task : tasks) {
            this.tasks.add(task);
            if (task.getPerson() != null) {
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
