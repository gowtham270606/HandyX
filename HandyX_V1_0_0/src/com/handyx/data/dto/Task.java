package com.handyx.data.dto;

public class Task {

    private long id;
    private long projectId;
    private long assignedToId;
    private long assignedById;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private long deadline;
    private long createdAt;
    private long updatedAt;

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum TaskPriority {
        LOW,
        MEDIUM,
        HIGH
    }

    public Task() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getProjectId() { return projectId; }
    public void setProjectId(long projectId) { this.projectId = projectId; }

    public long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(long assignedToId) { this.assignedToId = assignedToId; }

    public long getAssignedById() { return assignedById; }
    public void setAssignedById(long assignedById) { this.assignedById = assignedById; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public long getDeadline() { return deadline; }
    public void setDeadline(long deadline) { this.deadline = deadline; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
