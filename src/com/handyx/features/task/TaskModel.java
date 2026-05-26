package com.handyx.features.task;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.Project;
import com.handyx.data.dto.Task;
import com.handyx.data.repository.HandyXDB;
import com.handyx.util.DateHelper;

import java.util.List;

class TaskModel {

    private final TaskView taskView;

    TaskModel(TaskView taskView) {
        this.taskView = taskView;
    }

    String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) return "Task title cannot be empty";
        return null;
    }

    void createTask(String projectIdInput, String assigneeIdInput, String title,
                    String description, String priorityChoice,
                    String deadlineInput, Employee currentUser) {
        Long projectId = parseLong(projectIdInput);
        if (projectId == null || HandyXDB.getInstance().getProjectById(projectId) == null) {
            taskView.onTaskFailed("Invalid");
            return;
        }
        Long assigneeId = parseLong(assigneeIdInput);
        Employee assignee = HandyXDB.getInstance().getEmployeeById(assigneeId == null ? -1 : assigneeId);
        if (assignee == null) {
            taskView.onTaskFailed("Invalid");
            return;
        }

        if (currentUser.getRole() == Employee.Role.MANAGER) {
            if (assignee.getManagerUsername() == null || !assignee.getManagerUsername().equalsIgnoreCase(currentUser.getUsername())) {
                taskView.onTaskFailed("You can only assign tasks to your own employees");
                return;
            }
        }

        String titleError = validateTitle(title);
        if (titleError != null) { taskView.onTaskFailed(titleError); return; }

        Task.TaskPriority priority = parsePriority(priorityChoice);
        if (priority == null) { taskView.onTaskFailed("Invalid priority."); return; }

        Long deadline = DateHelper.parseDate(deadlineInput);
        if (deadline == null) { taskView.onTaskFailed("Invalid deadline."); return; }

        Task task = new Task();
        task.setProjectId(projectId);
        task.setAssignedToId(assigneeId);
        task.setAssignedById(currentUser.getId());
        task.setTitle(title.trim());
        task.setDescription(description == null ? "" : description.trim());
        task.setPriority(priority);
        task.setDeadline(deadline);

        Task saved = HandyXDB.getInstance().addTask(task);
        if (saved == null) { taskView.onTaskFailed("Try again."); return; }
        taskView.onTaskCreated(saved);
    }

    void loadTasks(Employee currentUser) {
        List<Task> tasks;
        if (currentUser.getRole() == Employee.Role.EMPLOYEE) {
            tasks = HandyXDB.getInstance().getTasksByEmployee(currentUser.getId());
        } else {
            tasks = HandyXDB.getInstance().getTasksByManager(currentUser.getUsername());
        }
        taskView.showTaskList(tasks);
    }

    void updateTaskStatus(String idInput, String statusChoice, Employee currentUser) {
        Long id = parseLong(idInput);
        if (id == null) { taskView.onTaskFailed("Invalid task ID"); return; }
        Task task = HandyXDB.getInstance().getTaskById(id);
        if (task == null) { taskView.onTaskFailed("Task not found"); return; }

        if (currentUser.getRole() == Employee.Role.EMPLOYEE
                && task.getAssignedToId() != currentUser.getId()) {
            taskView.onTaskFailed("You can only update your own tasks");
            return;
        }

        if (currentUser.getRole() == Employee.Role.MANAGER) {
            Employee assignee = HandyXDB.getInstance().getEmployeeById(task.getAssignedToId());
            if (assignee == null || assignee.getManagerUsername() == null || !assignee.getManagerUsername().equalsIgnoreCase(currentUser.getUsername())) {
                taskView.onTaskFailed("You can only update tasks of your own employees");
                return;
            }
        }

        Task.TaskStatus status = parseStatus(statusChoice);
        if (status == null) { taskView.onTaskFailed("Invalid status choice"); return; }

        task.setStatus(status);
        HandyXDB.getInstance().updateTask(task);
        taskView.onTaskUpdated(task);
    }

    void loadProjectsForPicker(Employee currentUser) {
        List<Project> projects = HandyXDB.getInstance().getProjectsByManager(currentUser.getId());
        taskView.showProjectPicker(projects);
    }

    void loadEmployeePicker(Employee currentUser) {
        List<Employee> employees = HandyXDB.getInstance().getEmployeesByManager(currentUser.getUsername());
        taskView.showEmployeePicker(employees);
    }

    private Task.TaskPriority parsePriority(String choice) {
        if (choice == null) return null;
        switch (choice.trim()) {
            case "1": return Task.TaskPriority.LOW;
            case "2": return Task.TaskPriority.MEDIUM;
            case "3": return Task.TaskPriority.HIGH;
            default:  return null;
        }
    }

    private Task.TaskStatus parseStatus(String choice) {
        if (choice == null) return null;
        switch (choice.trim()) {
            case "1": return Task.TaskStatus.PENDING;
            case "2": return Task.TaskStatus.IN_PROGRESS;
            case "3": return Task.TaskStatus.COMPLETED;
            case "4": return Task.TaskStatus.CANCELLED;
            default:  return null;
        }
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
