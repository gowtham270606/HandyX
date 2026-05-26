package com.handyx.features.project;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.Project;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

public class ProjectView {

    private final ProjectModel projectModel;
    private final Employee employee;
    private final Scanner scanner;

    public ProjectView(Employee employee) {
        this.projectModel = new ProjectModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Projects");
            System.out.println("1. View Projects");
            if (employee.getRole() == Employee.Role.MANAGER) {
                System.out.println("2. Add New Project");
                System.out.println("3. Update Project Status");
            }
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": projectModel.loadProjects(employee); break;
                case "2":
                    if (employee.getRole() == Employee.Role.MANAGER) promptAddProject();
                    else System.out.println("Invalid option.");
                    break;
                case "3":
                    if (employee.getRole() == Employee.Role.MANAGER) promptUpdateStatus();
                    else System.out.println("Invalid option.");
                    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private void promptAddProject() {
        System.out.println();
        System.out.println("Add New Project");
        projectModel.loadManagers();
        System.out.print("Project Name        : ");
        String name = scanner.nextLine();
        System.out.print("Description         : ");
        String description = scanner.nextLine();
        System.out.print("Manager ID          : ");
        String managerId = scanner.nextLine();
        System.out.print("Deadline (dd-MM-yyyy): ");
        String deadline = scanner.nextLine();
        projectModel.addProject(
                name == null ? null : name.trim(),
                description,
                managerId == null ? null : managerId.trim(),
                deadline == null ? null : deadline.trim(),
                employee);
    }

    private void promptUpdateStatus() {
        System.out.println();
        System.out.print("Project ID   : ");
        String id = scanner.nextLine();
        System.out.println("New Status:");
        System.out.println("1. ACTIVE");
        System.out.println("2. ON_HOLD");
        System.out.println("3. COMPLETED");
        System.out.print("Choose: ");
        String choice = scanner.nextLine();
        projectModel.updateProjectStatus(
                id == null ? null : id.trim(),
                choice == null ? null : choice.trim());
    }

    void showProjectList(List<Project> projects) {
        System.out.println();
        if (projects.isEmpty()) { System.out.println("No projects found."); return; }
        System.out.println("ID   Name                 Status       Deadline     Manager ID");
        System.out.println("---  -------------------  -----------  -----------  ------------");
        for (Project p : projects)
            System.out.printf("%-4d %-21s %-12s %-12s %d%n",
                    p.getId(),
                    DateHelper.truncate(p.getName(), 21),
                    p.getStatus(),
                    DateHelper.formatDate(p.getDeadline()),
                    p.getManagerId());
    }

    void showManagerList(List<Employee> leads) {
        if (leads.isEmpty()) { System.out.println("No Managers found."); return; }
        System.out.println("Managers:");
        for (Employee e : leads)
            System.out.printf("ID: %-4d  %s  [%s]%n", e.getId(), e.getName(), e.getEmployeeId());
    }

    void onProjectAdded(Project p) {
        System.out.println("Project '" + p.getName() + "' created (ID: " + p.getId() + ")");
    }

    void onProjectUpdated(Project p) {
        System.out.println("Project '" + p.getName() + "' status updated to " + p.getStatus());
    }

    void onProjectFailed(String message) {
        System.out.println("ERROR: " + message);
    }
}
