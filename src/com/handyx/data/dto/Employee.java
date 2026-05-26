package com.handyx.data.dto;

public class Employee {

    private long id;
    private String employeeId;
    private String name;
    private String username;
    private String password;
    private Role role;
    private String department;
    private boolean active;
    private long createdAt;

    public enum Role {
        MANAGER,
        EMPLOYEE
    }

    private String managerUsername;

    public Employee() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getManagerUsername() { return managerUsername; }
    public void setManagerUsername(String managerUsername) { this.managerUsername = managerUsername; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
