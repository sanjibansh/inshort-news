package com.example.newsapi.serviceImpl;

import com.example.newsapi.model.EventType;
import com.example.newsapi.model.NewsItem;
import com.example.newsapi.model.UserEvent;
import com.example.newsapi.repository.NewsRepository;
import com.example.newsapi.repository.UserEventRepository;
import com.example.newsapi.service.UserEventSimulationService;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
public class UserEventSimulationServiceImpl implements UserEventSimulationService {

    private final UserEventRepository userEventRepository;
    private final NewsRepository newsRepository; // Assume you have this

    public UserEventSimulationServiceImpl(UserEventRepository userEventRepository, NewsRepository newsRepository) {
        this.userEventRepository = userEventRepository;
        this.newsRepository = newsRepository;
    }

    public void simulateEvents() {
        Random random = new Random();

        List<NewsItem> availableNewsItems = newsRepository.findAll();

        for (int i = 0; i < 5; i++) {
            UserEvent event = new UserEvent();

            // Randomly pick a NewsItem from the available list
            NewsItem newsItem = availableNewsItems.get(random.nextInt(availableNewsItems.size()));
            event.setNewsItem(newsItem);

            // Assign a random EventType
            EventType[] eventTypes = EventType.values();
            EventType randomEventType = eventTypes[random.nextInt(eventTypes.length)];
            event.setEventType(randomEventType);

            // Generating random location near Delhi
            event.setLatitude(28.6139 + random.nextDouble());
            event.setLongitude(77.2090 + random.nextDouble());

            // Random timestamp within last 300 minutes
            event.setTimestamp(LocalDateTime.now().minusMinutes(random.nextInt(300)));

            userEventRepository.save(event);
        }
    }
}