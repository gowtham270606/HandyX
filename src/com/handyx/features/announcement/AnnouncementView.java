package com.handyx.features.announcement;

import com.handyx.data.dto.Announcement;
import com.handyx.data.dto.Employee;
import com.handyx.util.ConsoleInput;
import com.handyx.util.DateHelper;

import java.util.List;
import java.util.Scanner;

public class AnnouncementView {

    private final AnnouncementModel announcementModel;
    private final Employee employee;
    private final Scanner scanner;

    public AnnouncementView(Employee employee) {
        this.announcementModel = new AnnouncementModel(this);
        this.employee = employee;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("  [ Announcements ]");
            System.out.println("  1. View All Announcements");
            if (employee.getRole() != Employee.Role.EMPLOYEE) {
                System.out.println("  2. Post New Announcement");
            }
            System.out.println("  0. Back");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": announcementModel.loadAnnouncements(); break;
                case "2":
                    if (employee.getRole() != Employee.Role.EMPLOYEE) postAnnouncement();
                    else System.out.println("  Invalid option.");
                    break;
                case "0": return;
                default:  System.out.println("  Invalid option.");
            }
        }
    }

    private void postAnnouncement() {
        System.out.println();
        System.out.println(" Post Announcement ");
        System.out.print("  Title  : ");
        String title = scanner.nextLine();
        System.out.print("  Message: ");
        String message = scanner.nextLine();
        announcementModel.postAnnouncement(
                title == null ? null : title.trim(),
                message,
                employee);
    }

    void showAnnouncementList(List<Announcement> list) {
        System.out.println();
        if (list.isEmpty()) { System.out.println("  No announcements yet."); return; }
        for (Announcement a : list) {
            System.out.printf("  [#%d]  %s%n", a.getId(), a.getTitle());
            System.out.printf("  Posted: %s%n", DateHelper.formatDateTime(a.getPostedAt()));
            System.out.println("  " + a.getMessage());
        }
    }

    void onAnnouncementPosted(Announcement a) {
        System.out.println("  Announcement posted: '" + a.getTitle() + "' (ID: " + a.getId() + ")");
    }
    void onAnnouncementFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
