package com.handyx.features.leave;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.LeaveRequest;
import com.handyx.data.repository.HandyXDB;
import com.handyx.util.DateHelper;

import java.util.List;

class LeaveModel {

    private final LeaveView leaveView;

    LeaveModel(LeaveView leaveView) {
        this.leaveView = leaveView;
    }

    void applyLeave(String fromInput, String toInput, String reason, Employee currentUser) {
        if (reason == null || reason.trim().isEmpty()) {
            leaveView.onLeaveFailed("cannot be empty");
            return;
        }
        Long from = DateHelper.parseDate(fromInput);
        if (from == null) { leaveView.onLeaveFailed("Invalid fromat"); return; }
        Long to = DateHelper.parseDate(toInput);
        if (to == null) { leaveView.onLeaveFailed("Invalid to date"); return; }
        if (to < from) { leaveView.onLeaveFailed("To date must be after from date"); return; }

        LeaveRequest lr = new LeaveRequest();
        lr.setEmployeeId(currentUser.getId());
        lr.setReason(reason.trim());
        lr.setFromDate(from);
        lr.setToDate(to);
        lr.setStatus(LeaveRequest.LeaveStatus.PENDING);

        LeaveRequest saved = HandyXDB.getInstance().addLeaveRequest(lr);
        if (saved == null) { leaveView.onLeaveFailed("Try again."); return; }
        leaveView.onLeaveApplied(saved);
    }

    void loadMyLeaves(Employee currentUser) {
        List<LeaveRequest> list = HandyXDB.getInstance().getLeaveRequestsByEmployee(currentUser.getId());
        leaveView.showLeaveList(list);
    }

    void loadPendingLeaves(Employee currentUser) {
        List<LeaveRequest> list = HandyXDB.getInstance().getPendingLeaveRequestsByManager(currentUser.getUsername());
        leaveView.showPendingLeaves(list);
    }

    void reviewLeave(String idInput, String decision, String note, Employee reviewer) {
        Long id = parseLong(idInput);
        if (id == null) { leaveView.onLeaveFailed("Invalid leave request ID"); return; }

        LeaveRequest lr = HandyXDB.getInstance().getLeaveRequestById(id);
        if (lr == null) { leaveView.onLeaveFailed("Leave request not found"); return; }

        Employee requester = HandyXDB.getInstance().getEmployeeById(lr.getEmployeeId());
        if (requester == null || requester.getManagerUsername() == null || !requester.getManagerUsername().equalsIgnoreCase(reviewer.getUsername())) {
            leaveView.onLeaveFailed("You can only review leave requests from your own employees");
            return;
        }

        if (lr.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            leaveView.onLeaveFailed("already reviewed");
            return;
        }

        LeaveRequest.LeaveStatus status = parseDecision(decision);
        if (status == null) { leaveView.onLeaveFailed("Enter 1 or 2 "); return; }

        lr.setStatus(status);
        lr.setReviewedById(reviewer.getId());
        lr.setReviewNote(note == null ? "" : note.trim());
        lr.setReviewedAt(System.currentTimeMillis());

        HandyXDB.getInstance().updateLeaveRequest(lr);
        leaveView.onLeaveReviewed(lr);
    }

    private LeaveRequest.LeaveStatus parseDecision(String choice) {
        if (choice == null) return null;
        switch (choice.trim()) {
            case "1": return LeaveRequest.LeaveStatus.APPROVED;
            case "2": return LeaveRequest.LeaveStatus.REJECTED;
            default:  return null;
        }
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
