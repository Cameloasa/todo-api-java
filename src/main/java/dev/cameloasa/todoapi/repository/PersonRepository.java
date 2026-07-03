package dev.cameloasa.todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.cameloasa.todoapi.domanin.entity.Person;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    //find all persons with no task

    @Query("select p from Person p where size(p.tasks) = 0")
    List<Person> findIdlePeople();
}
