package com.example.newsapi.service;

import com.example.newsapi.model.CategoryType;
import com.example.newsapi.model.NewsItem;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Transactional
public interface NewsService {
    List<NewsItem> searchNewsByTitleAndDescription(String query);
    List<NewsItem> getNewsByCategory(List<String> categories);
    List<NewsItem> getNewsBySource(List<String> source);
    List<NewsItem> getNewsByScore(double threshold);
    List<NewsItem> getNewsBySearchQuery(String query);
    List<NewsItem> getNewsNearby(double latitude, double longitude, double radiusInKm);
    List<NewsItem> getTrendingNewsNearby(double latitude, double longitude, double radiusInKm);

}

