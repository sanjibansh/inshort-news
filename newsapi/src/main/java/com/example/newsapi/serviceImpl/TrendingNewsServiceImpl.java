package com.example.newsapi.serviceImpl;

import com.example.newsapi.dto.NewsItemDTO;
import com.example.newsapi.model.NewsItem;
import com.example.newsapi.model.UserEvent;
import com.example.newsapi.repository.UserEventRepository;
import com.example.newsapi.service.CacheTrendingNewsService;
import com.example.newsapi.service.TrendingNewsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrendingNewsServiceImpl implements TrendingNewsService {

    private final UserEventRepository eventRepository;
    private final CacheTrendingNewsService cacheTrendingNewsService;

    public TrendingNewsServiceImpl(UserEventRepository eventRepository, CacheTrendingNewsService cacheTrendingNewsService) {
        this.eventRepository = eventRepository;
        this.cacheTrendingNewsService = cacheTrendingNewsService;
    }

    @Override
    public List<NewsItemDTO> getTrendingNews(double lat, double lon, int limit) {
        List<NewsItemDTO> trendingFromCache = cacheTrendingNewsService.getTrendingNewsFromCache(lat, lon);
        System.out.println("======== Size of cache ========> " + (trendingFromCache.size()));
        if (!trendingFromCache.isEmpty()) {
            System.out.println("Results from cache ..........");
            return trendingFromCache;
        }

        LocalDateTime recentThreshold = LocalDateTime.now().minusHours(6);
        List<UserEvent> recentEvents = eventRepository.findByTimestampAfter(recentThreshold);

        Map<UUID, Double> scoreMap = new HashMap<>();

        for (UserEvent event : recentEvents) {
            NewsItem news = event.getNewsItem();
            double distance = haversine(lat, lon, event.getLatitude(), event.getLongitude());
            double locationWeight = 1.0 / (1.0 + distance);

            double baseScore = switch (event.getEventType()) {
                case CLICK -> 2.0;
                case VIEW -> 1.0;
                case SHARE -> 3.0;
            };

            double score = baseScore * locationWeight;
            scoreMap.merge(news.getId(), score, Double::sum);
        }

        List<NewsItem> trendingResults = scoreMap.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> recentEvents.stream()
                        .filter(e -> e.getNewsItem().getId().equals(entry.getKey()))
                        .findFirst()
                        .map(UserEvent::getNewsItem)
                        .orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<NewsItemDTO> dtoList = trendingResults.stream()
                .map(NewsItemDTO::fromEntity)
                .collect(Collectors.toList());

        cacheTrendingNewsService.saveTrendingNewsToCache(lat, lon, dtoList);

        return dtoList;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

//    // Optional: Only needed if full object conversion is necessary
//    private NewsItem dtoToEntity(NewsItemDTO dto) {
//        NewsItem item = new NewsItem();
//        item.setId(dto.getId());
//        item.setUrl(dto.getUrl());
//        item.setTitle(dto.getTitle());
//        item.setDescription(dto.getDescription());
//        item.setSummary(dto.getSummary());
//        item.setCategories(dto.getCategories());
//        item.setSource(dto.getSource());
//        item.setPublicationDate(dto.getPublicationDate());
//        item.setRelevanceScore(dto.getRelevanceScore());
//        item.setLatitude(dto.getLatitude());
//        item.setLongitude(dto.getLongitude());
//        return item;
//    }
}
