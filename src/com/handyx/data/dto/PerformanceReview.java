package com.handyx.data.dto;

public class PerformanceReview {

    private long id;
    private long employeeId;
    private long reviewedById;
    private int rating;         // 1 to 5
    private String feedback;
    private long reviewedAt;

    public PerformanceReview() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { this.employeeId = employeeId; }

    public long getReviewedById() { return reviewedById; }
    public void setReviewedById(long reviewedById) { this.reviewedById = reviewedById; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public long getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(long reviewedAt) { this.reviewedAt = reviewedAt; }
}
