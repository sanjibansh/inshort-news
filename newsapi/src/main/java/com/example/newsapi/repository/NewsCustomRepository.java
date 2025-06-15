package com.example.newsapi.repository;

import com.example.newsapi.model.NewsItem;

import java.util.List;

public interface NewsCustomRepository {
    List<NewsItem> searchNewsItems(String query);
}
