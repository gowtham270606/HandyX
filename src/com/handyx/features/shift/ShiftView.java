package com.handyx.features.shift;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.Shift;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

public class ShiftView {

    private final ShiftModel shiftModel;
    private final Employee employee;
    private final Scanner scanner;

    public ShiftView(Employee employee) {
        this.shiftModel = new ShiftModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Shift Management");
            System.out.println("1.My Shifts");
            if (employee.getRole() != Employee.Role.EMPLOYEE) {
                System.out.println("2. Assign Shift to Employee");
                System.out.println("3. View Shifts by Date");
            }
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": shiftModel.loadMyShifts(employee); break;
                case "2":
                    if (employee.getRole() != Employee.Role.EMPLOYEE) promptAssignShift();
                    else System.out.println("Invalid option.");
                    break;
                case "3":
                    if (employee.getRole() != Employee.Role.EMPLOYEE) promptViewByDate();
                    else System.out.println("Invalid option.");
                    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private void promptAssignShift() {
        System.out.println();
        System.out.println("Assign Shift");
        shiftModel.loadEmployeePicker(employee);
        System.out.print("Employee ID       : ");
        String empId = scanner.nextLine();
        System.out.print("Date (dd-MM-yyyy) : ");
        String date = scanner.nextLine();
        System.out.println("Shift: ");
        System.out.print("Choose: ");
        String shift = scanner.nextLine();
        shiftModel.assignShift(
                empId == null ? null : empId.trim(),
                date == null ? null : date.trim(),
                shift == null ? null : shift.trim(),
                employee);
    }

    private void promptViewByDate() {
        System.out.print("Date (dd-MM-yyyy): ");
        String date = scanner.nextLine();
        shiftModel.loadShiftsByDate(date == null ? null : date.trim(), employee);
    }

    void showShiftList(List<Shift> shifts) {
        System.out.println();
        if (shifts.isEmpty()) { System.out.println("No shifts found."); return; }
        System.out.println("  ID   Emp ID  Date         Shift");
        for (Shift s : shifts)
            System.out.printf("  %-4d %-7d %-12s %s%n",
                    s.getId(),
                    s.getEmployeeId(),
                    DateHelper.formatDate(s.getDate()),
                    s.getShiftType());
    }

    void showEmployeePicker(List<Employee> employees) {
        if (employees.isEmpty()) { System.out.println("No employees found."); return; }
        System.out.println("Employees:");
        for (Employee e : employees)
            System.out.printf("ID: %-4d  %s%n", e.getId(), e.getName());
    }

    void onShiftAssigned(Employee emp, Shift shift) {
        System.out.println("  " + emp.getName() + " assigned "
                + shift.getShiftType() + " shift on " + DateHelper.formatDate(shift.getDate()));
    }

    void onShiftFailed(String message) {
        System.out.println("ERROR: " + message);
    }
}
