package com.handyx.features.task;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.Project;
import com.handyx.data.dto.Task;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

public class TaskView {

    private final TaskModel taskModel;
    private final Employee employee;
    private final Scanner scanner;

    public TaskView(Employee employee) {
        this.taskModel = new TaskModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("  Tasks ");
            System.out.println("  1. View Tasks");
            if (employee.getRole() != Employee.Role.EMPLOYEE) {
                System.out.println("  2. Create Task");
            }
            System.out.println("  3. Update Task Status");
            System.out.println("  0. Back");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": taskModel.loadTasks(employee); break;
                case "2":
                    if (employee.getRole() != Employee.Role.EMPLOYEE) createTask();
                    else System.out.println("  Invalid option.");
                    break;
                case "3": updateStatus(); break;
                case "0": return;
                default:  System.out.println("  Invalid option.");
            }
        }
    }

    private void createTask() {
        System.out.println();
        System.out.println("  Create Task");
        taskModel.loadProjectsForPicker(employee);
        System.out.print("  Project ID          : ");
        String projectId = scanner.nextLine();
        taskModel.loadEmployeePicker(employee);
        System.out.print("  Assign To (Emp ID)  : ");
        String assigneeId = scanner.nextLine();
        System.out.print("  Title               : ");
        String title = scanner.nextLine();
        System.out.print("  Description         : ");
        String description = scanner.nextLine();
        System.out.println("  Priority: 1=LOW  2=MEDIUM  3=HIGH");
        System.out.print("  Choose: ");
        String priority = scanner.nextLine();
        System.out.print("  Deadline (dd-MM-yyyy): ");
        String deadline = scanner.nextLine();
        taskModel.createTask(
                projectId == null ? null : projectId.trim(),
                assigneeId == null ? null : assigneeId.trim(),
                title == null ? null : title.trim(),
                description,
                priority == null ? null : priority.trim(),
                deadline == null ? null : deadline.trim(),
                employee);
    }

    private void updateStatus() {
        System.out.println();
        System.out.print("  Task ID    : ");
        String id = scanner.nextLine();
        System.out.println("  New Status: 1=PENDING  2=IN_PROGRESS  3=COMPLETED  4=CANCELLED");
        System.out.print("  Choose: ");
        String choice = scanner.nextLine();
        taskModel.updateTaskStatus(
                id == null ? null : id.trim(),
                choice == null ? null : choice.trim(),
                employee);
    }

    void showProjectPicker(List<Project> projects) {
        if (projects.isEmpty()) { System.out.println("  No projects found."); return; }
        System.out.println("  Projects:");
        for (Project p : projects)
            System.out.printf("    ID: %-4d  %s  [%s]%n", p.getId(), p.getName(), p.getStatus());
    }

    void showEmployeePicker(List<Employee> employees) {
        if (employees.isEmpty()) { System.out.println("  No employees found."); return; }
        System.out.println("  Employees:");
        for (Employee e : employees)
            System.out.printf("    ID: %-4d  %-20s  [%s]%n", e.getId(), e.getName(), e.getEmployeeId());
    }

    void showTaskList(List<Task> tasks) {
        System.out.println();
        if (tasks.isEmpty()) { System.out.println("  No tasks found."); return; }
        System.out.println("  ID   Title                  Priority  Status       Deadline");
        System.out.println("  ---  ---------------------  --------  -----------  ----------");
        for (Task t : tasks)
            System.out.printf("  %-4d %-23s %-9s %-12s %s%n",
                    t.getId(),
                    DateHelper.truncate(t.getTitle(), 23),
                    t.getPriority(),
                    t.getStatus(),
                    DateHelper.formatDate(t.getDeadline()));
    }

    void onTaskCreated(Task t) {
        System.out.println("  Task '" + t.getTitle() + "' created ID: " + t.getId() + "");
    }

    void onTaskUpdated(Task t) {
        System.out.println("  Task " + t.getId() + " updated to " + t.getStatus());
    }

    void onTaskFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
