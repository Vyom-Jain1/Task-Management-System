package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Collections;

@Service
public class TaskServiceImpl implements TaskService {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            logger.info("Retrieved {} tasks", tasks.size());
            return tasks;
        } catch (DataAccessException e) {
            logger.error("Database error fetching all tasks: {}", e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error fetching all tasks: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        try {
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID: {}", id);
                throw new IllegalArgumentException("Invalid task ID: " + id);
            }
            return taskRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Task not found with id: {}", id);
                        return new RuntimeException("Task not found with id: " + id);
                    });
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            logger.error("Database error fetching task with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Database error fetching task: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching task with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error fetching task: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        try {
            validateTask(task);
            
            // Set default values
            if (task.getStatus() == null) {
                task.setStatus(TaskStatus.TODO);
                logger.debug("Set default status to TODO for new task");
            }
            if (task.getPriority() == null) {
                task.setPriority(3); // Default to low priority
                logger.debug("Set default priority to 3 for new task");
            }
            if (task.getUserId() == null) {
                task.setUserId(1L); // Default user ID for single-user mode
                logger.debug("Set default userId to 1 for new task");
            }
            
            Task savedTask = taskRepository.save(task);
            logger.info("Successfully created task with ID: {}", savedTask.getId());
            return savedTask;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating task: {}", e.getMessage());
            throw new IllegalArgumentException("Validation error: " + e.getMessage());
        } catch (DataAccessException e) {
            logger.error("Database error creating task: {}", e.getMessage());
            throw new RuntimeException("Database error creating task: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error creating task: {}", e.getMessage());
            throw new RuntimeException("Error creating task: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task task) {
        try {
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID for update: {}", id);
                throw new IllegalArgumentException("Invalid task ID: " + id);
            }
            
            Task existingTask = getTaskById(id);
            validateTask(task);
            
            // Update task fields
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            existingTask.setPriority(task.getPriority());
            existingTask.setDeadline(task.getDeadline());
            existingTask.setAssignedTo(task.getAssignedTo());
            existingTask.setEstimatedHours(task.getEstimatedHours());
            existingTask.setActualHours(task.getActualHours());
            
            // Keep the existing userId - don't allow updating it
            // updatedAt will be set automatically by @PreUpdate
            
            Task updatedTask = taskRepository.save(existingTask);
            logger.info("Successfully updated task with ID: {}", id);
            return updatedTask;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error updating task with ID {}: {}", id, e.getMessage());
            throw new IllegalArgumentException("Validation error: " + e.getMessage());
        } catch (DataAccessException e) {
            logger.error("Database error updating task with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Database error updating task: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error updating task with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error updating task: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        try {
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID for deletion: {}", id);
                throw new IllegalArgumentException("Invalid task ID: " + id);
            }
            
            Task task = getTaskById(id);
            taskRepository.delete(task);
            logger.info("Successfully deleted task with ID: {}", id);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error deleting task with ID {}: {}", id, e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            logger.error("Database error deleting task with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Database error deleting task: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error deleting task with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting task: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                logger.warn("Status cannot be null or empty");
                throw new IllegalArgumentException("Status cannot be null or empty");
            }
            
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            List<Task> tasks = taskRepository.findByStatus(taskStatus);
            logger.info("Retrieved {} tasks with status: {}", tasks.size(), status);
            return tasks;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid task status: {}", status);
            throw new IllegalArgumentException("Invalid task status: " + status);
        } catch (DataAccessException e) {
            logger.error("Database error fetching tasks by status {}: {}", status, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error fetching tasks by status {}: {}", status, e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> searchTasksByTitle(String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                logger.warn("Search title cannot be null or empty");
                throw new IllegalArgumentException("Search title cannot be null or empty");
            }
            
            List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title.trim());
            logger.info("Found {} tasks matching title: {}", tasks.size(), title);
            return tasks;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error searching tasks: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            logger.error("Database error searching tasks by title {}: {}", title, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error searching tasks by title {}: {}", title, e.getMessage());
            return Collections.emptyList();
        }
    }

    private void validateTask(Task task) {
        try {
            if (task == null) {
                throw new IllegalArgumentException("Task cannot be null");
            }
            if (!StringUtils.hasText(task.getTitle())) {
                throw new IllegalArgumentException("Task title cannot be empty");
            }
            if (task.getTitle().length() > 200) {
                throw new IllegalArgumentException("Task title cannot exceed 200 characters");
            }
            if (task.getDescription() != null && task.getDescription().length() > 1000) {
                throw new IllegalArgumentException("Task description cannot exceed 1000 characters");
            }
            if (task.getStatus() != null) {
                try {
                    TaskStatus.valueOf(task.getStatus().toString());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid task status: " + task.getStatus());
                }
            }
            if (task.getPriority() != null && (task.getPriority() < 1 || task.getPriority() > 3)) {
                throw new IllegalArgumentException("Priority must be between 1 and 3");
            }
            logger.debug("Task validation successful");
        } catch (IllegalArgumentException e) {
            logger.warn("Task validation failed: {}", e.getMessage());
            throw e;
        }
    }
}
