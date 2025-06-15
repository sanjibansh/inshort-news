package com.example.newsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
//import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder


@Table(name = "news_item", indexes = {
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_published_at", columnList = "publication_date")
})

public class NewsItem {

    @Id
    private UUID id;

    @Lob
    private String url;

    private String title;

    @Column(length = 5000)
    private String description;

    @Column(length = 3000)
    private String summary; // Cached LLM-generated summary (optional, used later)

    @ElementCollection
    @CollectionTable(
            name = "news_categories",
            joinColumns = @JoinColumn(name = "news_item_id")
    )
    @Column(name = "category")
    @JsonProperty("category")
    private List<String> categories = new ArrayList<>();

    @JsonProperty("source_name")
    private String source;

    @JsonProperty("publication_date")
    private LocalDateTime publicationDate;

    @JsonProperty("relevance_score")
    private double relevanceScore;

    private double latitude;

    private double longitude;

//    private LocalDateTime publishedAt;

    public UUID getId() {
        return id;
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

    public  List<String> getCategories() {
        return categories;
    }

    public String getSource() {
        return source;
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

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public  void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
