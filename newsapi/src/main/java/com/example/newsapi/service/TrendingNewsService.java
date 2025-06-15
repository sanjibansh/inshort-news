package com.example.newsapi.service;

import com.example.newsapi.dto.NewsItemDTO;
import java.util.List;

public interface TrendingNewsService {
    List<NewsItemDTO> getTrendingNews(double lat, double lon, int limit);
}
