package dev.cameloasa.todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cameloasa.todoapi.domanin.entity.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    //select tasks contain title
    List<Task> findByTitleContaining(String title);

    // select tasks by person's id
    List<Task> findByPersonId(Long personId);

    // select tasks by status
    List<Task> findByDone(boolean done);

    // select tasks by date between start and end
    List<Task> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);

    // select all unassigned tasks
    List<Task> findByPersonIsNull();

    // select all unfinished tasks
    List<Task> findByDoneFalse();

    // select all unfinished and overdue tasks
    @Query("SELECT t FROM Task t WHERE t.done = false AND t.deadline < :currentDate")
    List<Task> findUnfinishedAndOverdueTasks(@Param("currentDate") LocalDate currentDate);
}
