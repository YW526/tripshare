package com.inhatc.tripshare.dto;

import java.util.List;

public class TripResponse {
    private Long id;
    private String title;
    private String author;
    private Long authorUserId;
    private String destination;
    private String startDate;
    private String endDate;
    private String description;
    private int likeCount;
    private int reviewCount;
    private boolean mine;
    private boolean saved;
    private List<ScheduleItemResponse> scheduleItems;

    public TripResponse(Long id, String title, String author, Long authorUserId, String destination,
                        String startDate, String endDate, String description,
                        int likeCount, int reviewCount, boolean mine, boolean saved,
                        List<ScheduleItemResponse> scheduleItems) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorUserId = authorUserId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.likeCount = likeCount;
        this.reviewCount = reviewCount;
        this.mine = mine;
        this.saved = saved;
        this.scheduleItems = scheduleItems;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public String getDestination() {
        return destination;
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

    public int getLikeCount() {
        return likeCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isSaved() {
        return saved;
    }

    public List<ScheduleItemResponse> getScheduleItems() {
        return scheduleItems;
    }
}
