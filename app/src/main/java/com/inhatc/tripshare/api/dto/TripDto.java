package com.inhatc.tripshare.api.dto;

import java.util.List;

public class TripDto {
    public Long id;
    public String title;
    public String author;
    public Long authorUserId;
    public String destination;
    public String startDate;
    public String endDate;
    public String description;
    public int likeCount;
    public int reviewCount;
    public boolean mine;
    public boolean saved;
    public List<ScheduleItemDto> scheduleItems;
}
