package com.handyx.features.home;

import com.handyx.data.dto.Employee;
import com.handyx.data.repository.HandyXDB;

import java.util.List;

class EmployeeManagerModel {

    private final EmployeeManagerView employeeManagerView;

    EmployeeManagerModel(EmployeeManagerView employeeManagerView) {
        this.employeeManagerView = employeeManagerView;
    }

    String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Name cannot be empty";
        return null;
    }

    String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty";
        if (username.trim().length() < 3) return "Username must be at least 3 characters";
        if (HandyXDB.getInstance().getEmployeeByUsername(username) != null)
            return "Username '" + username.trim() + "' is already taken";
        return null;
    }

    String validatePassword(String password) {
        if (password == null || password.isEmpty()) return "Password cannot be empty";
        if (password.length() < 6) return "Password must be at least 6 characters";
        return null;
    }

    Employee.Role parseRole(String input) {
        if (input == null) return null;
        switch (input.trim().toUpperCase()) {
            case "MANAGER":   return Employee.Role.MANAGER;
            case "EMPLOYEE":  return Employee.Role.EMPLOYEE;
            default:          return null;
        }
    }

    void createEmployee(String name, String username, String password,
                        String department, Employee.Role role, Employee currentUser) {
        String nameError = validateName(name);
        if (nameError != null) { employeeManagerView.onFailed(nameError); return; }

        String usernameError = validateUsername(username);
        if (usernameError != null) { employeeManagerView.onFailed(usernameError); return; }

        String passwordError = validatePassword(password);
        if (passwordError != null) { employeeManagerView.onFailed(passwordError); return; }

        if (role == null) {
            employeeManagerView.onFailed("Invalid role. Use MANAGER or EMPLOYEE");
            return;
        }

        Employee emp = new Employee();
        emp.setName(name.trim());
        emp.setUsername(username.trim());
        emp.setPassword(password);
        emp.setDepartment(department == null ? "" : department.trim());
        emp.setRole(role);
        if (role == Employee.Role.EMPLOYEE) {
            emp.setManagerUsername(currentUser.getUsername());
        }

        Employee saved = HandyXDB.getInstance().addEmployee(emp);
        if (saved == null) { employeeManagerView.onFailed("Could not create employee. Try again."); return; }
        employeeManagerView.onEmployeeCreated(saved);
    }

    void loadAllEmployees(Employee currentUser) {
        List<Employee> list = HandyXDB.getInstance().getEmployeesByManager(currentUser.getUsername());
        employeeManagerView.showEmployeeList(list);
    }

    void deactivateEmployee(String idInput, Employee currentUser) {
        Long id = parseLong(idInput);
        if (id == null) { employeeManagerView.onFailed("Invalid employee ID"); return; }
        Employee emp = HandyXDB.getInstance().getEmployeeById(id);
        if (emp == null) { employeeManagerView.onFailed("Employee not found"); return; }

        if (emp.getRole() == Employee.Role.EMPLOYEE) {
            if (emp.getManagerUsername() == null || !emp.getManagerUsername().equalsIgnoreCase(currentUser.getUsername())) {
                employeeManagerView.onFailed("You can only deactivate your own employees");
                return;
            }
        } else if (emp.getRole() == Employee.Role.MANAGER && emp.getId() != currentUser.getId()) {
             employeeManagerView.onFailed("You cannot deactivate other managers");
             return;
        }

        if (!emp.isActive()) { employeeManagerView.onFailed("Employee is already inactive"); return; }
        emp.setActive(false);
        employeeManagerView.onEmployeeUpdated(emp);
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
