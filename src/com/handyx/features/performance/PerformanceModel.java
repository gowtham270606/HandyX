package com.handyx.features.performance;

import com.handyx.data.dto.Employee;
import com.handyx.data.dto.PerformanceReview;
import com.handyx.data.repository.HandyXDB;

import java.util.List;

class PerformanceModel {

    private final PerformanceView performanceView;

    PerformanceModel(PerformanceView performanceView) {
        this.performanceView = performanceView;
    }

    void addReview(String empIdInput, String ratingInput,
                   String feedback, Employee reviewer) {
        Long empId = parseLong(empIdInput);
        Employee emp = HandyXDB.getInstance().getEmployeeById(empId == null ? -1 : empId);
        if (emp == null) {
            performanceView.onReviewFailed("Invalid or unknown employee ID");
            return;
        }

        if (reviewer.getRole() == Employee.Role.MANAGER) {
            if (emp.getManagerUsername() == null || !emp.getManagerUsername().equalsIgnoreCase(reviewer.getUsername())) {
                performanceView.onReviewFailed("You can only review your own employees");
                return;
            }
        }

        if (empId == reviewer.getId()) {
            performanceView.onReviewFailed("You cannot review yourself");
            return;
        }

        Integer rating = parseRating(ratingInput);
        if (rating == null) { performanceView.onReviewFailed("Rating must be between 1 and 5"); return; }

        if (feedback == null || feedback.trim().isEmpty()) {
            performanceView.onReviewFailed("Feedback cannot be empty");
            return;
        }

        PerformanceReview review = new PerformanceReview();
        review.setEmployeeId(empId);
        review.setReviewedById(reviewer.getId());
        review.setRating(rating);
        review.setFeedback(feedback.trim());

        PerformanceReview saved = HandyXDB.getInstance().addReview(review);
        if (saved == null) { performanceView.onReviewFailed("Could not save review. Try again."); return; }
        performanceView.onReviewAdded(emp, saved);
    }

    void loadMyReviews(Employee currentUser) {
        List<PerformanceReview> reviews =
                HandyXDB.getInstance().getReviewsByEmployee(currentUser.getId());
        performanceView.showReviewList(reviews);
    }

    void loadAllReviews(Employee currentUser) {
        List<PerformanceReview> reviews = HandyXDB.getInstance().getReviewsByManager(currentUser.getUsername());
        performanceView.showReviewList(reviews);
    }

    void loadEmployeePicker(Employee currentUser) {
        List<Employee> employees = HandyXDB.getInstance().getEmployeesByManager(currentUser.getUsername());
        performanceView.showEmployeePicker(employees);
    }

    private Integer parseRating(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try {
            int r = Integer.parseInt(input.trim());
            return (r >= 1 && r <= 5) ? r : null;
        } catch (NumberFormatException e) { return null; }
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
