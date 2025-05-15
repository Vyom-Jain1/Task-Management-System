package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks by status
    List<Task> findByStatus(TaskStatus status);

    // Find tasks created after a specific date
    List<Task> findByCreatedAtAfter(LocalDateTime date);

    // Find tasks by title containing a keyword
    List<Task> findByTitleContainingIgnoreCase(String keyword);

    // Custom SQL query to find tasks due soon (within next 24 hours)
    @Query("SELECT t FROM Task t WHERE t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findActiveTasks();

    // Custom SQL query to find tasks by status and created date range
    @Query("SELECT t FROM Task t WHERE t.status = :status AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Task> findByStatusAndDateRange(
            @Param("status") TaskStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Native SQL query to count tasks by status
    @Query(value = "SELECT status, COUNT(*) FROM tasks GROUP BY status", nativeQuery = true)
    List<Object[]> countTasksByStatus();
}