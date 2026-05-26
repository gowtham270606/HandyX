package com.handyx.data.repository;

import com.handyx.data.dto.Announcement;
import com.handyx.data.dto.Employee;
import com.handyx.data.dto.LeaveRequest;
import com.handyx.data.dto.PerformanceReview;
import com.handyx.data.dto.Project;
import com.handyx.data.dto.Shift;
import com.handyx.data.dto.Task;
import com.handyx.util.DateHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HandyXDB {

    private static HandyXDB instance = null;

    private HandyXDB() {
    }

    public static HandyXDB getInstance() {
        if (instance == null) instance = new HandyXDB();
        return instance;
    }

    //  primary keys
    private long employeePk = 0;
    private long projectPk = 0;
    private long taskPk = 0;
    private long leavePk = 0;
    private long shiftPk = 0;
    private long performancePk = 0;
    private long announcementPk = 0;

    //  tables
    private final List<Employee> employees = new ArrayList<>();
    private final List<Project> projects = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private final List<LeaveRequest> leaveRequests = new ArrayList<>();
    private final List<Shift> shifts = new ArrayList<>();
    private final List<PerformanceReview> reviews = new ArrayList<>();
    private final List<Announcement> announcements = new ArrayList<>();

    // Employee
    public Employee addEmployee(Employee e) {
        if (e == null) return null;
        employeePk++;
        e.setId(employeePk);
        e.setEmployeeId(String.format(Locale.ROOT, "EMP%04d", employeePk));
        e.setActive(true);
        if (e.getCreatedAt() == 0) e.setCreatedAt(System.currentTimeMillis());
        employees.add(e);
        return e;
    }

    public Employee getEmployeeByUsername(String username) {
        if (username == null) return null;
        String key = username.trim().toLowerCase(Locale.ROOT);
        for (Employee e : employees)
            if (e.getUsername() != null && e.getUsername().toLowerCase(Locale.ROOT).equals(key))
                return e;
        return null;
    }

    public Employee authenticate(String username, String password) {
        Employee e = getEmployeeByUsername(username);
        if (e == null || !e.isActive() || password == null || !password.equals(e.getPassword()))
            return null;
        return e;
    }

    public Employee getEmployeeById(long id) {
        for (Employee e : employees) if (e.getId() == id) return e;
        return null;
    }

    public List<Employee> getAllEmployees() { return new ArrayList<>(employees); }

    public List<Employee> getEmployeesByRole(Employee.Role role) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees)
            if (role == null || e.getRole() == role) result.add(e);
        return result;
    }

    public List<Employee> getEmployeesByManager(String managerUsername) {
        List<Employee> result = new ArrayList<>();
        if (managerUsername == null) return result;
        String key = managerUsername.trim().toLowerCase(Locale.ROOT);
        for (Employee e : employees)
            if (e.getManagerUsername() != null && e.getManagerUsername().toLowerCase(Locale.ROOT).equals(key))
                result.add(e);
        return result;
    }

    public boolean hasEmployees() { return !employees.isEmpty(); }

    //  Project
    public Project addProject(Project p) {
        if (p == null) return null;
        projectPk++;
        p.setId(projectPk);
        if (p.getStatus() == null) p.setStatus(Project.ProjectStatus.ACTIVE);
        if (p.getCreatedAt() == 0) p.setCreatedAt(System.currentTimeMillis());
        projects.add(p);
        return p;
    }

    public Project updateProject(Project p) {
        if (p == null) return null;
        for (int i = 0; i < projects.size(); i++)
            if (projects.get(i).getId() == p.getId()) { projects.set(i, p); return p; }
        return null;
    }

    public List<Project> getAllProjects() { return new ArrayList<>(projects); }

    public List<Project> getProjectsByStatus(Project.ProjectStatus status) {
        List<Project> result = new ArrayList<>();
        for (Project p : projects)
            if (status == null || p.getStatus() == status) result.add(p);
        return result;
    }

    public List<Project> getProjectsByManager(long managerId) {
        List<Project> result = new ArrayList<>();
        for (Project p : projects)
            if (p.getManagerId() == managerId) result.add(p);
        return result;
    }

    public List<Task> getTasksByManager(String managerUsername) {
        List<Task> result = new ArrayList<>();
        List<Employee> team = getEmployeesByManager(managerUsername);
        for (Employee e : team) {
            result.addAll(getTasksByEmployee(e.getId()));
        }
        return result;
    }

    public Project getProjectById(long id) {
        for (Project p : projects) if (p.getId() == id) return p;
        return null;
    }

    //  Task
    public Task addTask(Task t) {
        if (t == null) return null;
        taskPk++;
        t.setId(taskPk);
        long now = System.currentTimeMillis();
        if (t.getStatus() == null) t.setStatus(Task.TaskStatus.PENDING);
        if (t.getPriority() == null) t.setPriority(Task.TaskPriority.MEDIUM);
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        tasks.add(t);
        return t;
    }

    public Task updateTask(Task t) {
        if (t == null) return null;
        for (int i = 0; i < tasks.size(); i++)
            if (tasks.get(i).getId() == t.getId()) {
                t.setUpdatedAt(System.currentTimeMillis());
                tasks.set(i, t);
                return t;
            }
        return null;
    }

    public List<Task> getTasksByProject(long projectId) {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks) if (t.getProjectId() == projectId) result.add(t);
        return result;
    }

    public List<Task> getTasksByEmployee(long employeeId) {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks) if (t.getAssignedToId() == employeeId) result.add(t);
        return result;
    }

    public Task getTaskById(long id) {
        for (Task t : tasks) if (t.getId() == id) return t;
        return null;
    }

    public List<Task> getAllTasks() { return new ArrayList<>(tasks); }

    // Leaves
    public LeaveRequest addLeaveRequest(LeaveRequest lr) {
        if (lr == null) return null;
        leavePk++;
        lr.setId(leavePk);
        if (lr.getStatus() == null) lr.setStatus(LeaveRequest.LeaveStatus.PENDING);
        if (lr.getCreatedAt() == 0) lr.setCreatedAt(System.currentTimeMillis());
        leaveRequests.add(lr);
        return lr;
    }

    public LeaveRequest updateLeaveRequest(LeaveRequest lr) {
        if (lr == null) return null;
        for (int i = 0; i < leaveRequests.size(); i++)
            if (leaveRequests.get(i).getId() == lr.getId()) { leaveRequests.set(i, lr); return lr; }
        return null;
    }

    public List<LeaveRequest> getLeaveRequestsByEmployee(long employeeId) {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest lr : leaveRequests)
            if (lr.getEmployeeId() == employeeId) result.add(lr);
        return result;
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest lr : leaveRequests)
            if (lr.getStatus() == LeaveRequest.LeaveStatus.PENDING) result.add(lr);
        return result;
    }

    public List<LeaveRequest> getPendingLeaveRequestsByManager(String managerUsername) {
        List<LeaveRequest> result = new ArrayList<>();
        List<Employee> team = getEmployeesByManager(managerUsername);
        for (Employee e : team) {
            for (LeaveRequest lr : leaveRequests) {
                if (lr.getEmployeeId() == e.getId() && lr.getStatus() == LeaveRequest.LeaveStatus.PENDING) {
                    result.add(lr);
                }
            }
        }
        return result;
    }

    public List<LeaveRequest> getAllLeaveRequests() { return new ArrayList<>(leaveRequests); }

    public LeaveRequest getLeaveRequestById(long id) {
        for (LeaveRequest lr : leaveRequests) if (lr.getId() == id) return lr;
        return null;
    }

    // Shift
    public Shift addShift(Shift s) {
        if (s == null) return null;
        shiftPk++;
        s.setId(shiftPk);
        if (s.getCreatedAt() == 0) s.setCreatedAt(System.currentTimeMillis());
        shifts.add(s);
        return s;
    }

    public boolean hasShiftOnDate(long employeeId, long date) {
        for (Shift s : shifts)
            if (s.getEmployeeId() == employeeId && DateHelper.isSameDay(s.getDate(), date))
                return true;
        return false;
    }

    public List<Shift> getShiftsByEmployee(long employeeId) {
        List<Shift> result = new ArrayList<>();
        for (Shift s : shifts) if (s.getEmployeeId() == employeeId) result.add(s);
        return result;
    }

    public List<Shift> getShiftsByDate(long date) {
        List<Shift> result = new ArrayList<>();
        for (Shift s : shifts)
            if (DateHelper.isSameDay(s.getDate(), date)) result.add(s);
        return result;
    }

    public List<Shift> getShiftsByDateAndManager(long date, String managerUsername) {
        List<Shift> result = new ArrayList<>();
        List<Employee> team = getEmployeesByManager(managerUsername);
        for (Employee e : team) {
            for (Shift s : shifts) {
                if (s.getEmployeeId() == e.getId() && DateHelper.isSameDay(s.getDate(), date)) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    // Review
    public PerformanceReview addReview(PerformanceReview r) {
        if (r == null) return null;
        performancePk++;
        r.setId(performancePk);
        if (r.getReviewedAt() == 0) r.setReviewedAt(System.currentTimeMillis());
        reviews.add(r);
        return r;
    }

    public List<PerformanceReview> getReviewsByEmployee(long employeeId) {
        List<PerformanceReview> result = new ArrayList<>();
        for (PerformanceReview r : reviews)
            if (r.getEmployeeId() == employeeId) result.add(r);
        return result;
    }

    public List<PerformanceReview> getAllReviews() { return new ArrayList<>(reviews); }

    public List<PerformanceReview> getReviewsByManager(String managerUsername) {
        List<PerformanceReview> result = new ArrayList<>();
        List<Employee> team = getEmployeesByManager(managerUsername);
        for (Employee e : team) {
            result.addAll(getReviewsByEmployee(e.getId()));
        }
        return result;
    }

    //Announcement
    public Announcement addAnnouncement(Announcement a) {
        if (a == null) return null;
        announcementPk++;
        a.setId(announcementPk);
        if (a.getPostedAt() == 0) a.setPostedAt(System.currentTimeMillis());
        announcements.add(a);
        return a;
    }

    public List<Announcement> getAllAnnouncements() { return new ArrayList<>(announcements); }
}
