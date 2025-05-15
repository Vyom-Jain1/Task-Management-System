document.addEventListener("DOMContentLoaded", function () {
  // Initialize variables
  const taskForm = document.getElementById("taskForm");
  const searchInput = document.getElementById("searchInput");
  const statusFilter = document.getElementById("statusFilter");
  const sortFilter = document.getElementById("sortFilter");
  const taskList = document.getElementById("taskList");
  const noTasksMessage = document.getElementById("noTasksMessage");
  const addTaskModal = new bootstrap.Modal(
    document.getElementById("addTaskModal")
  );

  // Statistics elements
  const totalTasksElement = document.getElementById("totalTasks");
  const completedTasksElement = document.getElementById("completedTasks");
  const inProgressTasksElement = document.getElementById("inProgressTasks");

  // Global Variables
  let tasks = [];
  let filteredTasks = [];
  window.tasks = tasks;
  window.filteredTasks = filteredTasks;

  // Load tasks on page load
  loadTasks();

  // Event Listeners
  document
    .getElementById("saveTask")
    .addEventListener("click", handleTaskSubmit);
  searchInput.addEventListener("input", handleSearch);
  statusFilter.addEventListener("change", handleFilter);
  sortFilter.addEventListener("change", handleSort);

  // Helper to track which cell is being edited
  let editingCell = null;

  // Functions
  async function loadTasks() {
    try {
      const response = await fetch("/api/tasks");
      tasks = await response.json();
      filteredTasks = [...tasks];
      window.tasks = tasks;
      window.filteredTasks = filteredTasks;
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
      const deadlineDate = task.deadline ? new Date(task.deadline) : null;
      const timeStatus = getTimeStatus(task);
      const now = new Date();
      const daysUntilDeadline = deadlineDate
        ? Math.ceil((deadlineDate - now) / (1000 * 60 * 60 * 24))
        : null;

      // Add classes for visual indicators
      if (daysUntilDeadline !== null) {
        if (daysUntilDeadline < 0) {
          row.classList.add("overdue");
        } else if (daysUntilDeadline <= 3) {
          row.classList.add("due-soon");
        }
      }

      // Title cell
      const titleCell = document.createElement("td");
      titleCell.textContent = task.title;
      titleCell.className = "editable-cell";
      titleCell.onclick = function (e) {
        if (editingCell) return;
        editingCell = titleCell;
        const input = document.createElement("input");
        input.type = "text";
        input.value = task.title;
        input.className = "form-control form-control-sm editable-field";
        input.onblur = function () {
          if (input.value !== task.title) {
            window.updateTaskField(task.id, "title", input.value);
          }
          titleCell.textContent = input.value;
          editingCell = null;
        };
        input.onkeydown = function (e) {
          if (e.key === "Enter") input.blur();
        };
        titleCell.textContent = "";
        titleCell.appendChild(input);
        input.focus();
      };
      row.appendChild(titleCell);

      // Description cell
      const descCell = document.createElement("td");
      descCell.textContent = task.description || "";
      descCell.className = "editable-cell";
      descCell.onclick = function (e) {
        if (editingCell) return;
        editingCell = descCell;
        const textarea = document.createElement("textarea");
        textarea.value = task.description || "";
        textarea.className = "form-control form-control-sm editable-field";
        textarea.onblur = function () {
          if (textarea.value !== task.description) {
            window.updateTaskField(task.id, "description", textarea.value);
          }
          descCell.textContent = textarea.value;
          editingCell = null;
        };
        textarea.onkeydown = function (e) {
          if (e.key === "Enter" && !e.shiftKey) textarea.blur();
        };
        descCell.textContent = "";
        descCell.appendChild(textarea);
        textarea.focus();
      };
      row.appendChild(descCell);

      // Status cell
      const statusCell = document.createElement("td");
      statusCell.className = "editable-cell";
      statusCell.innerHTML = `<span class="status-badge status-${
        task.status
      }">${task.status.replace("_", " ")}</span>`;
      statusCell.onclick = function (e) {
        if (editingCell) return;
        editingCell = statusCell;
        const select = document.createElement("select");
        select.className = "form-select form-select-sm editable-field";
        [
          { value: "TODO", label: "To Do" },
          { value: "IN_PROGRESS", label: "In Progress" },
          { value: "COMPLETED", label: "Completed" },
          { value: "CANCELLED", label: "Cancelled" },
        ].forEach((opt) => {
          const option = document.createElement("option");
          option.value = opt.value;
          option.textContent = opt.label;
          if (task.status === opt.value) option.selected = true;
          select.appendChild(option);
        });
        select.onblur = function () {
          if (select.value !== task.status) {
            window.updateTaskStatus(task.id, select.value);
          }
          statusCell.innerHTML = `<span class=\"status-badge status-${
            select.value
          }\">${select.options[select.selectedIndex].text}</span>`;
          editingCell = null;
        };
        select.onchange = function () {
          select.blur();
        };
        statusCell.textContent = "";
        statusCell.appendChild(select);
        select.focus();
      };
      row.appendChild(statusCell);

      // Deadline cell
      const deadlineCell = document.createElement("td");
      deadlineCell.className = "editable-cell";
      deadlineCell.textContent = deadlineDate
        ? deadlineDate.toLocaleString()
        : "No deadline";
      deadlineCell.onclick = function (e) {
        if (editingCell) return;
        editingCell = deadlineCell;
        const input = document.createElement("input");
        input.type = "datetime-local";
        input.className = "form-control form-control-sm editable-field";
        input.value = task.deadline
          ? new Date(task.deadline).toISOString().slice(0, 16)
          : "";
        input.onblur = function () {
          const newVal = input.value
            ? new Date(input.value).toISOString()
            : null;
          if (newVal !== task.deadline) {
            window.updateTaskField(task.id, "deadline", newVal);
          }
          deadlineCell.textContent = input.value
            ? new Date(input.value).toLocaleString()
            : "No deadline";
          editingCell = null;
        };
        input.onkeydown = function (e) {
          if (e.key === "Enter") input.blur();
        };
        deadlineCell.textContent = "";
        deadlineCell.appendChild(input);
        input.focus();
      };
      row.appendChild(deadlineCell);

      // Time status
      const timeStatusCell = document.createElement("td");
      timeStatusCell.innerHTML = timeStatus;
      row.appendChild(timeStatusCell);

      // Created at
      const createdAtCell = document.createElement("td");
      createdAtCell.textContent = new Date(task.createdAt).toLocaleString();
      row.appendChild(createdAtCell);

      // Actions
      const actionsCell = document.createElement("td");
      actionsCell.innerHTML = `<button class="btn btn-sm btn-danger btn-action" onclick="deleteTask(${task.id})"><i class="fas fa-trash"></i></button>`;
      row.appendChild(actionsCell);

      taskList.appendChild(row);
    });
  }

  function getTimeStatus(task) {
    if (task.status === "COMPLETED") {
      return '<span class="text-success">Completed</span>';
    }
    if (task.status === "CANCELLED") {
      return '<span class="text-muted">Cancelled</span>';
    }
    if (!task.deadline) {
      return '<span class="text-muted">No deadline</span>';
    }
    const now = new Date();
    const deadline = new Date(task.deadline);
    const daysUntilDeadline = Math.ceil(
      (deadline - now) / (1000 * 60 * 60 * 24)
    );
    if (daysUntilDeadline < 0) {
      const daysOverdue = Math.abs(daysUntilDeadline);
      return `<span class="text-danger">${daysOverdue} day${
        daysOverdue !== 1 ? "s" : ""
      } overdue</span>`;
    } else if (daysUntilDeadline === 0) {
      return '<span class="text-warning">Due today</span>';
    } else if (daysUntilDeadline <= 3) {
      return `<span class="text-warning">${daysUntilDeadline} day${
        daysUntilDeadline !== 1 ? "s" : ""
      } left</span>`;
    } else {
      return `<span class="text-success">${daysUntilDeadline} day${
        daysUntilDeadline !== 1 ? "s" : ""
      } left</span>`;
    }
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
    const deadline = document.getElementById("deadline").value;

    if (!title) {
      showAlert("Title is required", "warning");
      return;
    }

    try {
      const taskData = {
        title: title,
        description: description || null,
        status: status,
        priority: 3, // Default priority
        deadline: deadline ? new Date(deadline).toISOString() : null,
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
      showAlert(error.message || "Error creating task", "danger");
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
      } else if (sortBy === "deadline") {
        // Handle tasks without deadlines
        if (!a.deadline && !b.deadline) return 0;
        if (!a.deadline) return 1;
        if (!b.deadline) return -1;
        return new Date(a.deadline) - new Date(b.deadline);
      } else if (sortBy === "title") {
        return a.title.localeCompare(b.title);
      } else if (sortBy === "status") {
        return a.status.localeCompare(b.status);
      }
      return 0;
    });
    updateUI();
  }

  // Add function to check and update task statuses
  function checkAndUpdateTaskStatuses() {
    const now = new Date();
    tasks.forEach(async (task) => {
      if (task.status === "TODO" && task.deadline) {
        const deadline = new Date(task.deadline);
        const daysUntilDeadline = Math.ceil(
          (deadline - now) / (1000 * 60 * 60 * 24)
        );

        // If deadline is within 3 days, update status to IN_PROGRESS
        if (daysUntilDeadline <= 3 && daysUntilDeadline > 0) {
          try {
            const updatedTask = await updateTask(task.id, {
              ...task,
              status: "IN_PROGRESS",
            });
            tasks = tasks.map((t) => (t.id === task.id ? updatedTask : t));
            filteredTasks = [...tasks];
            updateUI();
          } catch (error) {
            console.error("Error updating task status:", error);
          }
        }
      }
    });
  }

  // Call status check every minute
  setInterval(checkAndUpdateTaskStatuses, 60000);

  // Initial status check
  checkAndUpdateTaskStatuses();

  // Global functions for button actions
  window.deleteTask = async function (taskId) {
    if (confirm("Are you sure you want to delete this task?")) {
      try {
        await deleteTask(taskId);
      } catch (error) {
        console.error("Error deleting task:", error);
      }
    }
  };

  // Add function to handle direct status updates
  window.updateTaskStatus = async function (taskId, newStatus) {
    try {
      const task = tasks.find((t) => t.id === taskId);
      if (!task) return;
      // Ensure all required fields are present
      const payload = {
        id: task.id,
        title: task.title,
        description: task.description,
        status: newStatus,
        priority: task.priority || 3,
        deadline: task.deadline || null,
        assignedTo: task.assignedTo || null,
        estimatedHours: task.estimatedHours || null,
        actualHours: task.actualHours || null,
        createdAt: task.createdAt,
        updatedAt: task.updatedAt,
      };
      const updatedTask = await updateTask(taskId, payload);
      tasks = tasks.map((t) => (t.id === taskId ? updatedTask : t));
      filteredTasks = [...tasks];
      updateUI();
      showAlert(`Task status updated to ${newStatus}`, "success");
    } catch (error) {
      console.error("Error updating task status:", error);
      if (error.response) {
        error.response
          .text()
          .then((text) => console.error("Backend error:", text));
      }
      showAlert("Error updating task status", "danger");
    }
  };

  // Add function to handle field updates
  window.updateTaskField = async function (taskId, field, value) {
    try {
      const task = tasks.find((t) => t.id === taskId);
      if (!task) return;
      // Ensure all required fields are present
      const payload = {
        id: task.id,
        title: field === "title" ? value : task.title,
        description: field === "description" ? value : task.description,
        status: task.status || "TODO",
        priority: task.priority || 3,
        deadline: field === "deadline" ? value : task.deadline || null,
        assignedTo: task.assignedTo || null,
        estimatedHours: task.estimatedHours || null,
        actualHours: task.actualHours || null,
        createdAt: task.createdAt,
        updatedAt: task.updatedAt,
      };
      const updatedTask = await updateTask(taskId, payload);
      tasks = tasks.map((t) => (t.id === taskId ? updatedTask : t));
      filteredTasks = [...tasks];
      updateUI();
      showAlert(`Task ${field} updated successfully`, "success");
    } catch (error) {
      console.error(`Error updating task ${field}:`, error);
      showAlert(`Error updating task ${field}`, "danger");
    }
  };

  // Add function to remove deadline
  window.removeDeadline = async function (taskId) {
    try {
      const task = tasks.find((t) => t.id === taskId);
      if (!task) return;

      const updatedTask = await updateTask(taskId, {
        ...task,
        deadline: null,
      });

      tasks = tasks.map((t) => (t.id === taskId ? updatedTask : t));
      filteredTasks = [...tasks];
      updateUI();
      showAlert("Deadline removed successfully", "success");
    } catch (error) {
      console.error("Error removing deadline:", error);
      showAlert("Error removing deadline", "danger");
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
    window.tasks = tasks;
    window.filteredTasks = filteredTasks;
  }

  function updateTaskList() {
    displayTasks(filteredTasks);
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
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to create task");
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

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to update task");
      }

      const updatedTask = await response.json();
      return updatedTask;
    } catch (error) {
      console.error("Error in updateTask:", error);
      throw error;
    }
  }

  async function deleteTask(id) {
    try {
      const response = await fetch(`/api/tasks/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to delete task");
      }

      tasks = tasks.filter((task) => task.id !== id);
      filteredTasks = [...tasks];
      updateUI();
      showAlert("Task deleted successfully", "success");
    } catch (error) {
      console.error("Error in deleteTask:", error);
      showAlert(error.message || "Error deleting task", "danger");
    }
  }

  window.displayTasks = displayTasks;
});
