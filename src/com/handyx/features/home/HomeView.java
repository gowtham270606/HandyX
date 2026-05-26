package com.handyx.features.home;

import com.handyx.data.dto.Employee;
import com.handyx.features.announcement.AnnouncementView;
import com.handyx.features.leave.LeaveView;
import com.handyx.features.performance.PerformanceView;
import com.handyx.features.project.ProjectView;
import com.handyx.features.shift.ShiftView;
import com.handyx.features.task.TaskView;
import com.handyx.util.ConsoleInput;

import java.util.Scanner;

public class HomeView {

    private final HomeModel homeModel;
    private final Employee employee;
    private final Scanner scanner;

    public HomeView(Employee employee) {
        this.homeModel = new HomeModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        homeModel.init(employee);
    }

    void showUnauthorized() {
        System.out.println("  Your role is not configured. Contact System.");
    }

    // MANAGER
    void showManagerMenu() {
        while (true) {
            System.out.println();
            System.out.println("Manager Dashboard" + employee.getName());
            System.out.println("1. Manage Projects");
            System.out.println("2. Manage Tasks");
            System.out.println("3. Manage Employees");
            System.out.println("4. Leave Requests");
            System.out.println("5. Shift Management");
            System.out.println("6. Performance Reviews");
            System.out.println("7. Announcements");
            System.out.println("8. Sign Out");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": new ProjectView(employee).init();      break;
                case "2": new TaskView(employee).init();         break;
                case "3": new EmployeeManagerView(employee).init(); break;
                case "4": new LeaveView(employee).init();        break;
                case "5": new ShiftView(employee).init();        break;
                case "6": new PerformanceView(employee).init();  break;
                case "7": new AnnouncementView(employee).init(); break;
                case "8":
                    System.out.println("Signed out.");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    // EMPLOYEE
    void showEmployeeMenu() {
        while (true) {
            System.out.println();
            System.out.println("Employee Dashboard" + employee.getName());
            System.out.println("1. My Tasks");
            System.out.println("2. My Leave Requests");
            System.out.println("3. My Shifts");
            System.out.println("4. My Performance");
            System.out.println("5. Announcements");
            System.out.println("6. Sign Out");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": new TaskView(employee).init();         break;
                case "2": new LeaveView(employee).init();        break;
                case "3": new ShiftView(employee).init();        break;
                case "4": new PerformanceView(employee).init();  break;
                case "5": new AnnouncementView(employee).init(); break;
                case "6":
                    System.out.println("Signed out.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
