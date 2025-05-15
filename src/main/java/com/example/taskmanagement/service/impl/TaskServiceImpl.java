package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        try {
            validateTask(task);

            // Set default values
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            if (task.getStatus() == null) {
                task.setStatus(TaskStatus.TODO);
            }
            if (task.getPriority() == null) {
                task.setPriority(3); // Default to low priority
            }

            return taskRepository.save(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error creating task: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task task) {
        try {
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
            existingTask.setUpdatedAt(LocalDateTime.now());

            return taskRepository.save(existingTask);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error updating task: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        try {
            Task task = getTaskById(id);
            taskRepository.delete(task);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            return taskRepository.findByStatus(taskStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task status: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> searchTasksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Search title cannot be null or empty");
        }

        try {
            return taskRepository.findByTitleContainingIgnoreCase(title.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error searching tasks: " + e.getMessage());
        }
    }

    private void validateTask(Task task) {
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
    }
}