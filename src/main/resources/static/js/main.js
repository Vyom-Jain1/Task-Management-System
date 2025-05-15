document.addEventListener("DOMContentLoaded", function () {
  // Initialize variables
  const taskForm = document.getElementById("taskForm");
  const editTaskForm = document.getElementById("editTaskForm");
  const searchInput = document.getElementById("searchInput");
  const statusFilter = document.getElementById("statusFilter");
  const sortFilter = document.getElementById("sortFilter");
  const taskList = document.getElementById("taskList");
  const noTasksMessage = document.getElementById("noTasksMessage");
  const addTaskModal = new bootstrap.Modal(
    document.getElementById("addTaskModal")
  );
  const editTaskModal = new bootstrap.Modal(
    document.getElementById("editTaskModal")
  );

  // Statistics elements
  const totalTasksElement = document.getElementById("totalTasks");
  const completedTasksElement = document.getElementById("completedTasks");
  const inProgressTasksElement = document.getElementById("inProgressTasks");

  // Global Variables
  let tasks = [];
  let filteredTasks = [];

  // Load tasks on page load
  loadTasks();

  // Event Listeners
  document
    .getElementById("saveTask")
    .addEventListener("click", handleTaskSubmit);
  document
    .getElementById("saveEditTask")
    .addEventListener("click", handleTaskUpdate);
  searchInput.addEventListener("input", handleSearch);
  statusFilter.addEventListener("change", handleFilter);
  sortFilter.addEventListener("change", handleSort);

  // Functions
  async function loadTasks() {
    try {
      const response = await fetch("/api/tasks");
      tasks = await response.json();
      filteredTasks = [...tasks];
      updateUI();
    } catch (error) {
      console.error("Error loading tasks:", error);
      showAlert("Error loading tasks", "danger");
    }
  }

  function displayTasks(tasks) {
    taskList.innerHTML = "";

    if (tasks.length === 0) {
      noTasksMessage.classList.remove("d-none");
      return;
    }

    noTasksMessage.classList.add("d-none");
    tasks.forEach((task) => {
      const row = document.createElement("tr");
      row.innerHTML = `
                <td>${task.title}</td>
                <td>${task.description || ""}</td>
                <td><span class="status-badge status-${task.status}">${
        task.status
      }</span></td>
                <td>${new Date(task.createdAt).toLocaleString()}</td>
                <td>
                    <button class="btn btn-sm btn-primary btn-action" onclick="editTask(${
                      task.id
                    })">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-danger btn-action" onclick="deleteTask(${
                      task.id
                    })">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            `;
      taskList.appendChild(row);
    });
  }

  function updateStatistics(tasks) {
    totalTasksElement.textContent = tasks.length;
    completedTasksElement.textContent = tasks.filter(
      (task) => task.status === "COMPLETED"
    ).length;
    inProgressTasksElement.textContent = tasks.filter(
      (task) => task.status === "IN_PROGRESS"
    ).length;
  }

  async function handleTaskSubmit() {
    const title = document.getElementById("title").value.trim();
    const description = document.getElementById("description").value.trim();
    const status = document.getElementById("status").value;

    if (!title) {
      showAlert("Title is required", "warning");
      return;
    }

    try {
      const taskData = {
        title: title,
        description: description,
        status: status,
      };

      const newTask = await createTask(taskData);
      tasks.push(newTask);
      filteredTasks = [...tasks];

      // Reset form and close modal
      document.getElementById("taskForm").reset();
      const addTaskModal = bootstrap.Modal.getInstance(
        document.getElementById("addTaskModal")
      );
      if (addTaskModal) {
        addTaskModal.hide();
      }

      // Update UI
      updateUI();
      showAlert("Task created successfully", "success");
    } catch (error) {
      console.error("Error creating task:", error);
      showAlert("Error creating task: " + error.message, "danger");
    }
  }

  async function handleTaskUpdate() {
    const taskId = document.getElementById("editTaskId").value;
    const task = {
      title: document.getElementById("editTitle").value,
      description: document.getElementById("editDescription").value,
      status: document.getElementById("editStatus").value,
    };

    if (!task.title) {
      showAlert("Title is required", "warning");
      return;
    }

    try {
      const updatedTask = await updateTask(taskId, task);
      editTaskModal.hide();
      tasks = tasks.map((t) => (t.id === taskId ? updatedTask : t));
      filteredTasks = [...tasks];
      updateUI();
    } catch (error) {
      console.error("Error updating task:", error);
    }
  }

  async function handleSearch(e) {
    const searchTerm = e.target.value.trim().toLowerCase();
    if (searchTerm) {
      filteredTasks = tasks.filter(
        (task) =>
          task.title.toLowerCase().includes(searchTerm) ||
          task.description.toLowerCase().includes(searchTerm)
      );
    } else {
      filteredTasks = [...tasks];
    }
    updateUI();
  }

  async function handleFilter() {
    const status = statusFilter.value;
    filteredTasks = status
      ? tasks.filter((task) => task.status === status)
      : [...tasks];
    updateUI();
  }

  function handleSort() {
    const sortBy = sortFilter.value;
    filteredTasks.sort((a, b) => {
      if (sortBy === "createdAt") {
        return new Date(b.createdAt) - new Date(a.createdAt);
      } else if (sortBy === "title") {
        return a.title.localeCompare(b.title);
      } else if (sortBy === "status") {
        return a.status.localeCompare(b.status);
      }
      return 0;
    });
    updateUI();
  }

  // Global functions for button actions
  window.editTask = async function (taskId) {
    try {
      const task = tasks.find((t) => t.id === taskId);
      if (task) {
        document.getElementById("editTaskId").value = task.id;
        document.getElementById("editTitle").value = task.title;
        document.getElementById("editDescription").value =
          task.description || "";
        document.getElementById("editStatus").value = task.status;

        editTaskModal.show();
      }
    } catch (error) {
      console.error("Error loading task for edit:", error);
      showAlert("Error loading task", "danger");
    }
  };

  window.deleteTask = async function (taskId) {
    if (confirm("Are you sure you want to delete this task?")) {
      try {
        await deleteTask(taskId);
      } catch (error) {
        console.error("Error deleting task:", error);
      }
    }
  };

  function showAlert(message, type) {
    const alertDiv = document.createElement("div");
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed top-0 end-0 m-3`;
    alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
    document.body.appendChild(alertDiv);

    setTimeout(() => {
      alertDiv.remove();
    }, 3000);
  }

  // UI Updates
  function updateUI() {
    updateTaskList();
    updateStatistics(tasks);
    updateEmptyState();
  }

  function updateTaskList() {
    taskList.innerHTML = filteredTasks
      .map(
        (task) => `
        <tr>
            <td>${task.title}</td>
            <td>${task.description || "-"}</td>
            <td><span class="status-badge status-${task.status}">${
          task.status
        }</span></td>
            <td>${new Date(task.createdAt).toLocaleDateString()}</td>
            <td>
                <button class="action-btn edit-btn" onclick="editTask(${
                  task.id
                })">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="action-btn delete-btn" onclick="deleteTask(${
                  task.id
                })">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `
      )
      .join("");
  }

  function updateEmptyState() {
    if (filteredTasks.length === 0) {
      noTasksMessage.classList.remove("d-none");
      taskList.parentElement.classList.add("d-none");
    } else {
      noTasksMessage.classList.add("d-none");
      taskList.parentElement.classList.remove("d-none");
    }
  }

  // API Calls
  async function createTask(taskData) {
    try {
      const response = await fetch("/api/tasks", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(taskData),
      });

      if (!response.ok) {
        throw new Error("Failed to create task");
      }

      const newTask = await response.json();
      return newTask;
    } catch (error) {
      console.error("Error in createTask:", error);
      throw error;
    }
  }

  async function updateTask(id, taskData) {
    try {
      const response = await fetch(`/api/tasks/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(taskData),
      });
      const updatedTask = await response.json();
      const index = tasks.findIndex((task) => task.id === id);
      tasks[index] = updatedTask;
      filteredTasks = [...tasks];
      updateUI();
      showAlert("Task updated successfully", "success");
      return updatedTask;
    } catch (error) {
      showAlert("Error updating task", "danger");
      throw error;
    }
  }

  async function deleteTask(id) {
    try {
      const response = await fetch(`/api/tasks/${id}`, {
        method: "DELETE",
      });

      if (response.ok) {
        tasks = tasks.filter((task) => task.id !== id);
        filteredTasks = [...tasks];
        updateUI();
        showAlert("Task deleted successfully", "success");
      } else {
        throw new Error("Failed to delete task");
      }
    } catch (error) {
      console.error("Error deleting task:", error);
      showAlert("Error deleting task", "danger");
    }
  }
});
