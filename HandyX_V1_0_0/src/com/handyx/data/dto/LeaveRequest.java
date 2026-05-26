package com.handyx.data.dto;

public class LeaveRequest {

    private long id;
    private long employeeId;
    private long reviewedById;
    private String reason;
    private long fromDate;
    private long toDate;
    private LeaveStatus status;
    private String reviewNote;
    private long createdAt;
    private long reviewedAt;

    public enum LeaveStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public LeaveRequest() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { this.employeeId = employeeId; }

    public long getReviewedById() { return reviewedById; }
    public void setReviewedById(long reviewedById) { this.reviewedById = reviewedById; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public long getFromDate() { return fromDate; }
    public void setFromDate(long fromDate) { this.fromDate = fromDate; }

    public long getToDate() { return toDate; }
    public void setToDate(long toDate) { this.toDate = toDate; }

    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }

    public String getReviewNote() { return reviewNote; }
    public void setReviewNote(String reviewNote) { this.reviewNote = reviewNote; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(long reviewedAt) { this.reviewedAt = reviewedAt; }
}
