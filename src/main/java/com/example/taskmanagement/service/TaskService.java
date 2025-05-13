package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Task task);

    void deleteTask(Long id);

    List<Task> getTasksByStatus(String status);

    List<Task> searchTasksByTitle(String title);
}