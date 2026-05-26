package com.handyx.features.shift;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.Shift;
import com.handyx.data.repository.HandyXDB;
import com.handyx.util.DateHelper;

import java.util.List;

class ShiftModel {

    private final ShiftView shiftView;

    ShiftModel(ShiftView shiftView) {
        this.shiftView = shiftView;
    }

    void assignShift(String empIdInput, String dateInput,
                     String shiftChoice, Employee currentUser) {
        Long empId = parseLong(empIdInput);
        Employee emp = HandyXDB.getInstance().getEmployeeById(empId == null ? -1 : empId);
        if (emp == null) {
            shiftView.onShiftFailed("Invalid or unknown employee ID");
            return;
        }

        if (currentUser.getRole() == Employee.Role.MANAGER) {
            if (emp.getManagerUsername() == null || !emp.getManagerUsername().equalsIgnoreCase(currentUser.getUsername())) {
                shiftView.onShiftFailed("You can only assign shifts to your own employees");
                return;
            }
        }

        Long date = DateHelper.parseDate(dateInput);
        if (date == null) { shiftView.onShiftFailed("Invalid date. Use dd-MM-yyyy"); return; }

        if (HandyXDB.getInstance().hasShiftOnDate(emp.getId(), date)) {
            shiftView.onShiftFailed("Employee already has a shift on this date");
            return;
        }

        Shift.ShiftType shiftType = parseShift(shiftChoice);
        if (shiftType == null) { shiftView.onShiftFailed("Invalid shift. Use 1/2/3"); return; }

        Shift shift = new Shift();
        shift.setEmployeeId(emp.getId());
        shift.setAssignedById(currentUser.getId());
        shift.setDate(date);
        shift.setShiftType(shiftType);

        Shift saved = HandyXDB.getInstance().addShift(shift);
        if (saved == null) { shiftView.onShiftFailed("Could not assign shift. Try again."); return; }
        shiftView.onShiftAssigned(emp, saved);
    }

    void loadMyShifts(Employee currentUser) {
        List<Shift> shifts = HandyXDB.getInstance().getShiftsByEmployee(currentUser.getId());
        shiftView.showShiftList(shifts);
    }

    void loadShiftsByDate(String dateInput, Employee currentUser) {
        Long date = DateHelper.parseDate(dateInput);
        if (date == null) { shiftView.onShiftFailed("Invalid date. Use dd-MM-yyyy"); return; }
        List<Shift> shifts = HandyXDB.getInstance().getShiftsByDateAndManager(date, currentUser.getUsername());
        shiftView.showShiftList(shifts);
    }

    void loadEmployeePicker(Employee currentUser) {
        List<Employee> employees = HandyXDB.getInstance().getEmployeesByManager(currentUser.getUsername());
        shiftView.showEmployeePicker(employees);
    }

    private Shift.ShiftType parseShift(String choice) {
        if (choice == null) return null;
        switch (choice.trim()) {
            case "1": return Shift.ShiftType.MORNING;
            case "2": return Shift.ShiftType.AFTERNOON;
            case "3": return Shift.ShiftType.NIGHT;
            default:  return null;
        }
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
