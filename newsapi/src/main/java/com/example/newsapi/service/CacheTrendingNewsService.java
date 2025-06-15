package com.example.newsapi.service;

import com.example.newsapi.dto.NewsItemDTO;
import java.util.List;

public interface CacheTrendingNewsService {
    List<NewsItemDTO> getTrendingNewsFromCache(double lat, double lon);
    void saveTrendingNewsToCache(double lat, double lon, List<NewsItemDTO> items);
}
