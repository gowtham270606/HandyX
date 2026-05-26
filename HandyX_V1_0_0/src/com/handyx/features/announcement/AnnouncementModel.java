package com.handyx.features.announcement;

import com.handyx.data.dto.Announcement;
import com.handyx.data.dto.Employee;
import com.handyx.data.repository.HandyXDB;

import java.util.List;

class AnnouncementModel {

    private final AnnouncementView announcementView;

    AnnouncementModel(AnnouncementView announcementView) {
        this.announcementView = announcementView;
    }

    void postAnnouncement(String title, String message, Employee poster) {
        if (title == null || title.trim().isEmpty()) {
            announcementView.onAnnouncementFailed("Title cannot be empty");
            return;
        }
        if (message == null || message.trim().isEmpty()) {
            announcementView.onAnnouncementFailed("Message cannot be empty");
            return;
        }

        Announcement announcement = new Announcement();
        announcement.setPostedById(poster.getId());
        announcement.setTitle(title.trim());
        announcement.setMessage(message.trim());

        Announcement saved = HandyXDB.getInstance().addAnnouncement(announcement);
        if (saved == null) { announcementView.onAnnouncementFailed("Could not post. Try again."); return; }
        announcementView.onAnnouncementPosted(saved);
    }

    void loadAnnouncements() {
        List<Announcement> list = HandyXDB.getInstance().getAllAnnouncements();
        announcementView.showAnnouncementList(list);
    }
}
