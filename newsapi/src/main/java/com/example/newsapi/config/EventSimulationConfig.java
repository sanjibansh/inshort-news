package com.example.newsapi.config;

import com.example.newsapi.model.EventType;
import com.example.newsapi.model.UserEvent;
import com.example.newsapi.repository.NewsRepository;
import com.example.newsapi.repository.UserEventRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import com.example.newsapi.model.NewsItem;

@Configuration
public class EventSimulationConfig {

    private final UserEventRepository userEventRepository;
    private final NewsRepository newsRepository; // Assume you have this

    public EventSimulationConfig(UserEventRepository userEventRepository, NewsRepository newsRepository) {
        this.userEventRepository = userEventRepository;
        this.newsRepository = newsRepository;
    }

    @PostConstruct
    public void simulateEvents() {
        Random random = new Random();

        List<NewsItem> availableNewsItems = newsRepository.findAll(); // or fetch specific ones

        if (availableNewsItems.isEmpty()) {
            System.out.println(" No news items found. Cannot simulate events.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            UserEvent event = new UserEvent();

            NewsItem newsItem = availableNewsItems.get(random.nextInt(availableNewsItems.size()));
            event.setNewsItem(newsItem);

            // Assign a random EventType
            EventType[] eventTypes = EventType.values();
            EventType randomEventType = eventTypes[random.nextInt(eventTypes.length)];
            event.setEventType(randomEventType);

            // Generate random location
            event.setLatitude(28.6139 + random.nextDouble());
            event.setLongitude(77.2090 + random.nextDouble());

            // Random timestamp within last 300 minutes
            event.setTimestamp(LocalDateTime.now().minusMinutes(random.nextInt(300)));

            userEventRepository.save(event);
        }
    }
}