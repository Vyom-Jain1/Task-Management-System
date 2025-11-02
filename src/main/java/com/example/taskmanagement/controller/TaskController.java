package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            logger.info("Request received to get all tasks");
            List<Task> tasks = taskService.getAllTasks();
            logger.info("Successfully retrieved {} tasks", tasks.size());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error fetching all tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Long id) {
        try {
            logger.info("Request received to get task with ID: {}", id);
            
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID provided: {}", id);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid task ID");
                error.put("message", "Task ID must be a positive number");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Task task = taskService.getTaskById(id);
            logger.info("Successfully retrieved task with ID: {}", id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            logger.warn("Task not found with ID: {}. Error: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Task not found");
            error.put("message", "Task with ID " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error fetching task with ID {}: {}", id, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An error occurred while fetching the task");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createTask(@RequestBody Task task) {
        try {
            logger.info("Request received to create new task");
            
            if (task == null) {
                logger.warn("Attempted to create null task");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid request");
                error.put("message", "Task data is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Task createdTask = taskService.createTask(task);
            logger.info("Successfully created task with ID: {}", createdTask.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error creating task: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Validation error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An error occurred while creating the task");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            logger.info("Request received to update task with ID: {}", id);
            
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID provided for update: {}", id);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid task ID");
                error.put("message", "Task ID must be a positive number");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (task == null) {
                logger.warn("Attempted to update task with null data");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid request");
                error.put("message", "Task data is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Task updatedTask = taskService.updateTask(id, task);
            logger.info("Successfully updated task with ID: {}", id);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error updating task with ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Validation error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (RuntimeException e) {
            logger.warn("Task not found for update with ID: {}. Error: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Task not found");
            error.put("message", "Task with ID " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error updating task with ID {}: {}", id, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An error occurred while updating the task");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long id) {
        try {
            logger.info("Request received to delete task with ID: {}", id);
            
            if (id == null || id <= 0) {
                logger.warn("Invalid task ID provided for deletion: {}", id);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid task ID");
                error.put("message", "Task ID must be a positive number");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            taskService.deleteTask(id);
            logger.info("Successfully deleted task with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.warn("Task not found for deletion with ID: {}. Error: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Task not found");
            error.put("message", "Task with ID " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error deleting task with ID {}: {}", id, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An error occurred while deleting the task");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status) {
        try {
            logger.info("Request received to search tasks with title: {} and status: {}", title, status);
            List<Task> tasks;
            
            if (title != null && !title.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
                // Search by both title and status: first get by title, then filter by status
                logger.debug("Searching by both title and status");
                List<Task> titleResults = taskService.searchTasksByTitle(title);
                tasks = titleResults.stream()
                    .filter(task -> status.equalsIgnoreCase(task.getStatus().name()))
                    .collect(java.util.stream.Collectors.toList());
            } else if (title != null && !title.trim().isEmpty()) {
                logger.debug("Searching by title only");
                tasks = taskService.searchTasksByTitle(title);
            } else if (status != null && !status.trim().isEmpty()) {
                logger.debug("Searching by status only");
                tasks = taskService.getTasksByStatus(status);
            } else {
                logger.debug("No search parameters provided, returning all tasks");
                tasks = taskService.getAllTasks();
            }
            
            logger.info("Search completed. Found {} tasks", tasks.size());
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid search parameters: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid parameter");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            logger.error("Error searching tasks: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "An error occurred while searching tasks");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
