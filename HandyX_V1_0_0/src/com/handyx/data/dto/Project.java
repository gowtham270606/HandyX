package com.handyx.data.dto;

public class Project {

    private long id;
    private String name;
    private String description;
    private long managerId;
    private ProjectStatus status;
    private long deadline;
    private long createdAt;

    public enum ProjectStatus {
        ACTIVE,
        ON_HOLD,
        COMPLETED
    }

    public Project() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getManagerId() { return managerId; }
    public void setManagerId(long managerId) { this.managerId = managerId; }

    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }

    public long getDeadline() { return deadline; }
    public void setDeadline(long deadline) { this.deadline = deadline; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
