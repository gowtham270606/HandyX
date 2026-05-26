package com.handyx.features.leave;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.LeaveRequest;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

public class LeaveView {

    private final LeaveModel leaveModel;
    private final Employee employee;
    private final Scanner scanner;

    public LeaveView(Employee employee) {
        this.leaveModel = new LeaveModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Leave Management");
            System.out.println("1. Apply for Leave");
            if (employee.getRole() == Employee.Role.EMPLOYEE) {
                System.out.println("2. View Pending Review");
            } else {
                System.out.println("2. My Leave Requests");
                System.out.println("3. Review Pending Requests");
            }
            System.out.println("0. Back");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": applyLeave();   break;
                case "2": leaveModel.loadMyLeaves(employee); break;
                case "3":
                    if (employee.getRole() != Employee.Role.EMPLOYEE) {
                        leaveModel.loadPendingLeaves(employee);
                        reviewLeave();
                    } else System.out.println("Invalid option.");
                    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private void applyLeave() {
        System.out.println();
        System.out.println("Apply for Leave");
        System.out.print("From: ");
        String from = scanner.nextLine();
        System.out.print("To: ");
        String to = scanner.nextLine();
        System.out.print("Reason: ");
        String reason = scanner.nextLine();
        leaveModel.applyLeave(
                from == null ? null : from.trim(),
                to == null ? null : to.trim(),
                reason,
                employee);
    }

    private void reviewLeave() {
        System.out.println();
        System.out.print("Leave Request ID : ");
        String id = scanner.nextLine();
        System.out.println("Decision 1 APPROVE  2 REJECT");
        System.out.print("Choose: ");
        String decision = scanner.nextLine();
        System.out.print("Optional: ");
        String note = scanner.nextLine();
        leaveModel.reviewLeave(
                id == null ? null : id.trim(),
                decision == null ? null : decision.trim(),
                note,
                employee);
    }

    void showLeaveList(List<LeaveRequest> list) {
        System.out.println();
        if (list.isEmpty()) { System.out.println("No leave requests found."); return; }
        System.out.println("ID   From         To           Status     Reason");
        System.out.println("---  -----------  -----------  ---------  -------------------------");
        for (LeaveRequest lr : list)
            System.out.printf("%-4d %-12s %-12s %-10s %s%n",
                    lr.getId(),
                    DateHelper.formatDate(lr.getFromDate()),
                    DateHelper.formatDate(lr.getToDate()),
                    lr.getStatus(),
                    DateHelper.truncate(lr.getReason(), 25));
    }

    void showPendingLeaves(List<LeaveRequest> list) {
        System.out.println();
        if (list.isEmpty()) { System.out.println("No pending leave requests."); return; }
        System.out.println("Pending Leave Requests:");
        System.out.println("ID   Emp ID  From         To           Reason");
        for (LeaveRequest lr : list)
            System.out.printf("%-4d %-7d %-12s %-12s %s%n",
                    lr.getId(),
                    lr.getEmployeeId(),
                    DateHelper.formatDate(lr.getFromDate()),
                    DateHelper.formatDate(lr.getToDate()),
                    DateHelper.truncate(lr.getReason(), 25));
    }

    void onLeaveApplied(LeaveRequest lr) {
        System.out.println("Leave request submitted (ID: " + lr.getId() + ")");
    }

    void onLeaveReviewed(LeaveRequest lr) {
        System.out.println("Leave " + lr.getId() + " has been " + lr.getStatus());
    }

    void onLeaveFailed(String message) {
        System.out.println("ERROR: " + message);
    }
}
