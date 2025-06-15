package com.example.newsapi.dto;

import com.example.newsapi.model.NewsItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NewsItemDTO {
    private UUID id;
    private String url;
    private String title;
    private String description;
    private String summary;
    private List<String> categories;
    private String source;
    private LocalDateTime publicationDate;
    private double relevanceScore;
    private double latitude;
    private double longitude;

    // Getters
    public UUID getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getSource() {
        return source;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setRelevanceScore(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Mapping method from Entity to DTO
    public static NewsItemDTO fromEntity(NewsItem entity) {
        NewsItemDTO dto = new NewsItemDTO();
        dto.setId(entity.getId());
        dto.setUrl(entity.getUrl());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setSummary(entity.getSummary());
        dto.setCategories(entity.getCategories());
        dto.setSource(entity.getSource());
        dto.setPublicationDate(entity.getPublicationDate());
        dto.setRelevanceScore(entity.getRelevanceScore());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        return dto;
    }
}
