package com.handyx.features.home;

import com.handyx.data.dto.Employee;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

class EmployeeManagerView {

    private final EmployeeManagerModel employeeManagerModel;
    private final Employee currentUser;
    private final Scanner scanner;

    EmployeeManagerView(Employee currentUser) {
        this.employeeManagerModel = new EmployeeManagerModel(this);
        this.currentUser = currentUser;
        this.scanner = ConsoleInput.getScanner();
    }

    void init() {
        while (true) {
            System.out.println();
            System.out.println("  Manage Employees ");
            System.out.println("  1. View My Employees");
            System.out.println("  2. Add New Employee");
            System.out.println("  3. Deactivate Employee");
            System.out.println("  0. Back");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": employeeManagerModel.loadAllEmployees(currentUser); break;
                case "2": addEmployee();                                break;
                case "3": deactivate();                                 break;
                case "0": return;
                default:  System.out.println("  Invalid option.");
            }
        }
    }

    private void addEmployee() {
        System.out.println();
        System.out.print("Full Name              : ");
        String name = scanner.nextLine();
        System.out.print("Username               : ");
        String username = scanner.nextLine();
        System.out.print("Password               : ");
        String password = scanner.nextLine();
        System.out.print("Department             : ");
        String department = scanner.nextLine();
        System.out.print("Role (MANAGER/EMPLOYEE): ");
        String roleInput = scanner.nextLine();
        Employee.Role role = employeeManagerModel.parseRole(roleInput);
        employeeManagerModel.createEmployee(
                name == null ? null : name.trim(),
                username == null ? null : username.trim(),
                password,
                department,
                role,
                currentUser);
    }

    private void deactivate() {
        System.out.print("Employee ID to deactivate: ");
        String id = scanner.nextLine();
        employeeManagerModel.deactivateEmployee(id == null ? null : id.trim(), currentUser);
    }

    void showEmployeeList(List<Employee> employees) {
        System.out.println();
        if (employees.isEmpty()) { System.out.println("  No employees found."); return; }
        System.out.println("  ID   Emp ID     Name                 Role        Department       Status");
        for (Employee e : employees)
            System.out.printf("  %-4d %-10s %-21s %-11s %-17s %s%n",
                    e.getId(),
                    e.getEmployeeId(),
                    DateHelper.truncate(e.getName(), 21),
                    e.getRole(),
                    e.getDepartment() == null ? "" : e.getDepartment(),
                    e.isActive() ? "ACTIVE" : "INACTIVE");
    }

    void onEmployeeCreated(Employee e) {
        System.out.println("  Employee created: " + e.getName()
                + " [" + e.getEmployeeId() + "]  Role: " + e.getRole());
    }

    void onEmployeeUpdated(Employee e) {
        System.out.println("  " + e.getName() + " is now INACTIVE.");
    }

    void onFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
