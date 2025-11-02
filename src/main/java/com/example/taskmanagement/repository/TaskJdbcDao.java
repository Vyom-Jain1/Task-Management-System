package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

@Repository
public class TaskJdbcDao {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskJdbcDao.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Maps a SQL row to a Task object
    private final RowMapper<Task> taskRowMapper = new RowMapper<Task>() {
        @Override
        public Task mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            try {
                Task task = new Task();
                task.setId(rs.getLong("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                task.setPriority(rs.getInt("priority"));
                task.setDeadline(
                        rs.getTimestamp("deadline") != null ? rs.getTimestamp("deadline").toLocalDateTime() : null);
                task.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
                task.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                return task;
            } catch (SQLException e) {
                logger.error("Error mapping ResultSet to Task object: {}", e.getMessage());
                throw e;
            } catch (IllegalArgumentException e) {
                logger.error("Error converting task status: {}", e.getMessage());
                throw new SQLException("Invalid task status in database", e);
            }
        }
    };

    // Get all tasks
    public List<Task> findAllTasks() {
        try {
            return jdbcTemplate.query("SELECT * FROM tasks", taskRowMapper);
        } catch (DataAccessException e) {
            logger.error("Error fetching all tasks: {}", e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error fetching all tasks: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // Get a task by ID
    public Task findTaskById(Long id) {
        try {
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID provided: {}", id);
                return null;
            }
            return jdbcTemplate.queryForObject("SELECT * FROM tasks WHERE id = ?", taskRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Task not found with ID: {}", id);
            return null;
        } catch (DataAccessException e) {
            logger.error("Database error fetching task with ID {}: {}", id, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error fetching task with ID {}: {}", id, e.getMessage());
            return null;
        }
    }

    // Insert a new task
    public int insertTask(Task task) {
        try {
            if (task == null) {
                logger.warn("Attempted to insert null task");
                return 0;
            }
            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                logger.warn("Attempted to insert task with null or empty title");
                return 0;
            }
            return jdbcTemplate.update(
                    "INSERT INTO tasks (title, description, status, priority, deadline, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getPriority(),
                    task.getDeadline(), task.getCreatedAt(), task.getUpdatedAt());
        } catch (DataAccessException e) {
            logger.error("Database error inserting task: {}", e.getMessage());
            return 0;
        } catch (Exception e) {
            logger.error("Unexpected error inserting task: {}", e.getMessage());
            return 0;
        }
    }

    // Update an existing task
    public int updateTask(Task task) {
        try {
            if (task == null || task.getId() == null) {
                logger.warn("Attempted to update null task or task with null ID");
                return 0;
            }
            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                logger.warn("Attempted to update task with null or empty title");
                return 0;
            }
            return jdbcTemplate.update(
                    "UPDATE tasks SET title=?, description=?, status=?, priority=?, deadline=?, updated_at=? WHERE id=?",
                    task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getPriority(),
                    task.getDeadline(), task.getUpdatedAt(), task.getId());
        } catch (DataAccessException e) {
            logger.error("Database error updating task with ID {}: {}", task != null ? task.getId() : "null", e.getMessage());
            return 0;
        } catch (Exception e) {
            logger.error("Unexpected error updating task with ID {}: {}", task != null ? task.getId() : "null", e.getMessage());
            return 0;
        }
    }

    // Delete a task by ID
    public int deleteTask(Long id) {
        try {
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID provided for deletion: {}", id);
                return 0;
            }
            return jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
        } catch (DataAccessException e) {
            logger.error("Database error deleting task with ID {}: {}", id, e.getMessage());
            return 0;
        } catch (Exception e) {
            logger.error("Unexpected error deleting task with ID {}: {}", id, e.getMessage());
            return 0;
        }
    }
}
