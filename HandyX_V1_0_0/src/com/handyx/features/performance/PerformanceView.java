package com.handyx.features.performance;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.PerformanceReview;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

public class PerformanceView {

    private final PerformanceModel performanceModel;
    private final Employee employee;
    private final Scanner scanner;

    public PerformanceView(Employee employee) {
        this.performanceModel = new PerformanceModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("  [ Performance Reviews ]");
            if (employee.getRole() == Employee.Role.EMPLOYEE) {
                System.out.println("  1. My Reviews");
            } else {
                System.out.println("  1. Add Review");
                System.out.println("  2. View All Reviews");
            }
            System.out.println("  0. Back");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    if (employee.getRole() == Employee.Role.EMPLOYEE)
                        performanceModel.loadMyReviews(employee);
                    else addReview();
                    break;
                case "2":
                    if (employee.getRole() != Employee.Role.EMPLOYEE)
                        performanceModel.loadAllReviews(employee);
                    else System.out.println("  Invalid option.");
                    break;
                case "0": return;
                default:  System.out.println("  Invalid option.");
            }
        }
    }

    private void addReview() {
        System.out.println();
        System.out.println("  -- Add Performance Review --");
        performanceModel.loadEmployeePicker(employee);
        System.out.print("  Employee ID     : ");
        String empId = scanner.nextLine();
        System.out.print("  Rating (1-5)    : ");
        String rating = scanner.nextLine();
        System.out.print("  Feedback        : ");
        String feedback = scanner.nextLine();
        performanceModel.addReview(
                empId == null ? null : empId.trim(),
                rating == null ? null : rating.trim(),
                feedback,
                employee);
    }

    void showEmployeePicker(List<Employee> employees) {
        if (employees.isEmpty()) { System.out.println("No employees found."); return; }
        System.out.println("Employees:");
        for (Employee e : employees)
            System.out.printf("ID: %-4d  %s%n", e.getId(), e.getName());
    }

    void showReviewList(List<PerformanceReview> reviews) {
        System.out.println();
        if (reviews.isEmpty()) { System.out.println("  No reviews found."); return; }
        System.out.println("ID   Emp ID  Rating       Date         Feedback");
        for (PerformanceReview r : reviews)
            System.out.printf("%-4d %-7d %s  %-12s %s%n",
                    r.getId(),
                    r.getEmployeeId(),
                    DateHelper.stars(r.getRating()),
                    DateHelper.formatDate(r.getReviewedAt()),
                    DateHelper.truncate(r.getFeedback(), 25));
    }

    void onReviewAdded(Employee emp, PerformanceReview review) {
        System.out.println("Review saved for " + emp.getName()
                + " — Rating: " + DateHelper.stars(review.getRating()));
    }

    void onReviewFailed(String message) {
        System.out.println("ERROR: " + message);
    }
}
