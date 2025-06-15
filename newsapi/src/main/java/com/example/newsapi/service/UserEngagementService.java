package com.example.newsapi.service;

import com.example.newsapi.model.UserEvent;

import java.util.List;
import java.util.UUID;

public interface UserEngagementService {
    void addEvent(UserEvent event);
    List<UserEvent> getAllEvents();
    List<UserEvent> getEventsForArticle(UUID articleId);
    List<UserEvent> getRecentEventsWithinRadius(double lat, double lon, double radiusKm);
}

