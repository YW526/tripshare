package com.inhatc.tripshare.model;

import java.util.List;

public class Trip {
    private final int id;
    private final String title;
    private final String author;
    private final String location;
    private final String startDate;
    private final String endDate;
    private final String description;
    private final int imageRes;
    private final int likes;
    private final int comments;
    private final List<ScheduleItem> schedule;
    private final boolean mine;
    private final boolean saved;

    public Trip(int id, String title, String author, String location,
                String startDate, String endDate, String description,
                int imageRes, int likes, int comments,
                List<ScheduleItem> schedule, boolean mine, boolean saved) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.imageRes = imageRes;
        this.likes = likes;
        this.comments = comments;
        this.schedule = schedule;
        this.mine = mine;
        this.saved = saved;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public List<ScheduleItem> getSchedule() {
        return schedule;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getDateRange() {
        return startDate + " - " + endDate;
    }
}
