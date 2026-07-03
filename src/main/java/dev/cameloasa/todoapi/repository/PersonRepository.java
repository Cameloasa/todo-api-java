package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

  // Find a person by username
  Optional<Person> findByUsername(String username);

  // Find a person by user email
  Optional<Person> findByUserEmail(String email);

  // Check if a person exists by username
  boolean existsByUsername(String username);

  // Check if a person exists by user email
  boolean existsByUserEmail(String email);

  // Find persons by first name or last name containing a string (case insensitive)
  List<Person> findByFirstNameContainingIgnoreCase(String firstName);

  // Find persons by last name containing a string (case insensitive)
  List<Person> findByLastNameContainingIgnoreCase(String lastName);

  // find all persons with no task
  @Query("select p from Person p where size(p.tasks) = 0")
  List<Person> findIdlePeople();
}
