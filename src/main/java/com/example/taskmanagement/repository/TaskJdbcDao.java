package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskJdbcDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Maps a SQL row to a Task object
    private final RowMapper<Task> taskRowMapper = new RowMapper<Task>() {
        @Override
        public Task mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(TaskStatus.valueOf(rs.getString("status")));
            task.setPriority(rs.getInt("priority"));
            task.setDeadline(
                    rs.getTimestamp("deadline") != null ? rs.getTimestamp("deadline").toLocalDateTime() : null);
            task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return task;
        }
    };

    // Get all tasks
    public List<Task> findAllTasks() {
        return jdbcTemplate.query("SELECT * FROM tasks", taskRowMapper);
    }

    // Get a task by ID
    public Task findTaskById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM tasks WHERE id = ?", taskRowMapper, id);
    }

    // Insert a new task
    public int insertTask(Task task) {
        return jdbcTemplate.update(
                "INSERT INTO tasks (title, description, status, priority, deadline, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getPriority(),
                task.getDeadline(), task.getCreatedAt(), task.getUpdatedAt());
    }

    // Update an existing task
    public int updateTask(Task task) {
        return jdbcTemplate.update(
                "UPDATE tasks SET title=?, description=?, status=?, priority=?, deadline=?, updated_at=? WHERE id=?",
                task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getPriority(),
                task.getDeadline(), task.getUpdatedAt(), task.getId());
    }

    // Delete a task by ID
    public int deleteTask(Long id) {
        return jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }
}