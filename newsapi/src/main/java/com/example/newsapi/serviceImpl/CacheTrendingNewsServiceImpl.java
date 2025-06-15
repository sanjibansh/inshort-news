package com.example.newsapi.serviceImpl;

import com.example.newsapi.dto.NewsItemDTO;
import com.example.newsapi.service.CacheTrendingNewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CacheTrendingNewsServiceImpl implements CacheTrendingNewsService {

    private static final String TRENDING_KEY_PREFIX = "trending:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public CacheTrendingNewsServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private String locationKey(double lat, double lon) {
        // Round to 1 decimal ~ approx 11km x 11km grid
        String roundedLat = String.format("%.1f", lat);
        String roundedLon = String.format("%.1f", lon);
        return TRENDING_KEY_PREFIX + roundedLat + ":" + roundedLon;
    }

    @Override
    public List<NewsItemDTO> getTrendingNewsFromCache(double lat, double lon) {
        String key = locationKey(lat, lon);
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached == null) return Collections.emptyList();

        try {
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, NewsItemDTO.class);

            // Converting LinkedHashMap -> DTO
            List<NewsItemDTO> result = objectMapper.convertValue(cached, listType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void saveTrendingNewsToCache(double lat, double lon, List<NewsItemDTO> items) {
        System.out.println("Saving date to cache........");
        redisTemplate.opsForValue().set(locationKey(lat, lon), items);
        System.out.println("Saved data to cache........");
    }
}
