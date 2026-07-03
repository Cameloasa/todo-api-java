package dev.cameloasa.todoapi.repository;

import dev.cameloasa.todoapi.domanin.entity.Task;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks by title containing a specific string
    List<Task> findByTitleContaining(String title);

    // Find tasks by person's ID
    List<Task> findByPersonId(Long personId);

    // Find tasks by completion status
    List<Task> findByDone(boolean done);

    // Find tasks by deadline between two dates
    List<Task> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);

    // Find tasks where person is not assigned
    List<Task> findByPersonIsNull();

    // Find tasks that are not done
    List<Task> findByDoneFalse();

    // Find tasks that are not done and overdue
    @Query("SELECT t FROM Task t WHERE t.done = false AND t.deadline < :currentDate")
    List<Task> findUnfinishedAndOverdueTasks(@Param("currentDate") LocalDate currentDate);

    // Find tasks by person's ID and completion status
    List<Task> findByPersonIdAndDone(Long personId, boolean done);

    // Find tasks by deadline and completion status
    List<Task> findByDeadlineAndDone(LocalDate deadline, boolean done);

    // Find tasks that are overdue
    @Query("SELECT t FROM Task t WHERE t.deadline < CURRENT_DATE")
    List<Task> findOverdueTasks();

    // Find tasks by description containing a specific string
    List<Task> findByDescriptionContaining(String description);

    // Find tasks by person's ID and title containing a specific string
    List<Task> findByPersonIdAndTitleContaining(Long personId, String title);
}
