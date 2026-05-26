package com.handyx.features.project;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.Project;
import com.handyx.data.repository.HandyXDB;
import com.handyx.util.DateHelper;

import java.util.List;

class ProjectModel {

    private final ProjectView projectView;

    ProjectModel(ProjectView projectView) {
        this.projectView = projectView;
    }

    String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Project name cannot be empty";
        if (name.trim().length() < 3) return "Project name must be at least 3 characters";
        return null;
    }

    void addProject(String name, String description, String managerIdInput,
                    String deadlineInput, Employee currentUser) {
        String nameError = validateName(name);
        if (nameError != null) { projectView.onProjectFailed(nameError); return; }

        Long managerId = parseLong(managerIdInput);
        if (managerId == null) { projectView.onProjectFailed("Invalid Manager ID"); return; }
        Employee manager = HandyXDB.getInstance().getEmployeeById(managerId);
        if (manager == null || manager.getRole() != Employee.Role.MANAGER) {
            projectView.onProjectFailed("No Manager found with ID: " + managerId);
            return;
        }

        Long deadline = DateHelper.parseDate(deadlineInput);
        if (deadline == null) { projectView.onProjectFailed("Invalid deadline. Use dd-MM-yyyy"); return; }

        Project project = new Project();
        project.setName(name.trim());
        project.setDescription(description == null ? "" : description.trim());
        project.setManagerId(managerId);
        project.setDeadline(deadline);
        project.setStatus(Project.ProjectStatus.ACTIVE);

        Project saved = HandyXDB.getInstance().addProject(project);
        if (saved == null) { projectView.onProjectFailed("not create project. Try again."); return; }
        projectView.onProjectAdded(saved);
    }

    void loadProjects(Employee currentUser) {
        List<Project> projects = HandyXDB.getInstance().getAllProjects();
        projectView.showProjectList(projects);
    }

    void updateProjectStatus(String idInput, String statusChoice) {
        Long id = parseLong(idInput);
        if (id == null) { projectView.onProjectFailed("Invalid project ID"); return; }
        Project project = HandyXDB.getInstance().getProjectById(id);
        if (project == null) { projectView.onProjectFailed("Project not found"); return; }

        Project.ProjectStatus status = parseStatus(statusChoice);
        if (status == null) { projectView.onProjectFailed("Invalid status choice"); return; }

        project.setStatus(status);
        HandyXDB.getInstance().updateProject(project);
        projectView.onProjectUpdated(project);
    }

    void loadManagers() {
        List<Employee> managers = HandyXDB.getInstance().getEmployeesByRole(Employee.Role.MANAGER);
        projectView.showManagerList(managers);
    }

    private Project.ProjectStatus parseStatus(String choice) {
        if (choice == null) return null;
        switch (choice.trim()) {
            case "1": return Project.ProjectStatus.ACTIVE;
            case "2": return Project.ProjectStatus.ON_HOLD;
            case "3": return Project.ProjectStatus.COMPLETED;
            default:  return null;
        }
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
