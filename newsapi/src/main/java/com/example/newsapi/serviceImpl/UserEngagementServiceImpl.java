package com.example.newsapi.serviceImpl;

import com.example.newsapi.model.UserEvent;
import com.example.newsapi.service.UserEngagementService;
import org.springframework.stereotype.Service;
import com.example.newsapi.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserEngagementServiceImpl implements UserEngagementService {

    private final List<UserEvent> eventStore = new ArrayList<>();

    @Override
    public void addEvent(UserEvent event) {
        eventStore.add(event);
    }

    @Override
    public List<UserEvent> getAllEvents() {
        return new ArrayList<>(eventStore);
    }

    @Override
    public List<UserEvent> getEventsForArticle(UUID newsItemId) {
        return eventStore.stream()
                .filter(e -> {
                    UUID eventNewsItemId = e.getNewsItem().getId();
                    return eventNewsItemId != null && eventNewsItemId.equals(newsItemId);
                })
                .toList();
    }

    @Override
    public List<UserEvent> getRecentEventsWithinRadius(double lat, double lon, double radiusKm) {
        return eventStore.stream()
                .filter(e -> {
                    double distance = DistanceCalculator.calculateDistance(lat, lon, e.getLatitude(), e.getLongitude());
                    return distance <= radiusKm;
                })
                .toList();
    }
}

