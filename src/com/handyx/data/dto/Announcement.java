package com.handyx.data.dto;

public class Announcement {

    private long id;
    private long postedById;
    private String title;
    private String message;
    private long postedAt;

    public Announcement() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getPostedById() { return postedById; }
    public void setPostedById(long postedById) { this.postedById = postedById; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getPostedAt() { return postedAt; }
    public void setPostedAt(long postedAt) { this.postedAt = postedAt; }
}
