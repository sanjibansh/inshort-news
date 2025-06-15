package com.example.newsapi.config;

import com.example.newsapi.model.EventType;
import com.example.newsapi.model.NewsItem;
import com.example.newsapi.model.UserEvent;
import com.example.newsapi.repository.NewsRepository;
import com.example.newsapi.repository.UserEventRepository;
import com.example.newsapi.serviceImpl.NewsBulkLoaderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig {

    private final NewsBulkLoaderServiceImpl loaderService;
    private final UserEventRepository userEventRepository;
    private final NewsRepository newsItemRepository;

    public DataLoaderConfig(NewsBulkLoaderServiceImpl loaderService,
                            UserEventRepository userEventRepository,
                            NewsRepository newsItemRepository) {
        this.loaderService = loaderService;
        this.userEventRepository = userEventRepository;
        this.newsItemRepository = newsItemRepository;
    }

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            try {
                URI fileUri = getClass().getClassLoader()
                        .getResource("news_data.json")
                        .toURI();

                File file = new File(fileUri);
                if (!file.exists()) {
                    System.out.println("JSON file not found.");
                    return;
                }

                Path jsonPath = file.toPath();
                List<NewsItem> loadedNews = loaderService.loadNewsFromJson(jsonPath);
                System.out.println("Loaded " + loadedNews.size() + " news items.");

                loaderService.seedUserEvents(loadedNews, 2000);
                System.out.println("User events seeded.");

                seedUserEvents(); // optional if you want 5 random extra
            } catch (Exception e) {
                System.err.println("Error loading JSON file:");
                e.printStackTrace();
            }
        };
    }

    private void seedUserEvents() {
        List<NewsItem> availableNewsItems = newsItemRepository.findAll();
        if (availableNewsItems.isEmpty()) {
            System.out.println("No NewsItems to seed events.");
            return;
        }

        Random random = new Random();
        EventType[] eventTypes = EventType.values();

        for (int i = 0; i < 2000; i++) {
            UserEvent event = new UserEvent();
            NewsItem newsItem = availableNewsItems.get(random.nextInt(availableNewsItems.size()));
            event.setNewsItem(newsItem);
            event.setEventType(eventTypes[random.nextInt(eventTypes.length)]);
            event.setLatitude(21.6139 + random.nextDouble());
            event.setLongitude(72.2090 + random.nextDouble());
            event.setTimestamp(LocalDateTime.now().minusMinutes(random.nextInt(300)));

            userEventRepository.save(event);
        }
    }
}
