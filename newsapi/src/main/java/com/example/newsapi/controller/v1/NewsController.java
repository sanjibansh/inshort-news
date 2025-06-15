package com.example.newsapi.controller.v1;

import com.example.newsapi.dto.LLMResponse;
import com.example.newsapi.model.NewsItem;
import com.example.newsapi.service.LLMService;
import com.example.newsapi.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/v1/news")
public class NewsController {

    private final NewsService newsService;
    private final LLMService llmService;

    public NewsController(NewsService newsService, LLMService llmService) {
        this.newsService = newsService;
        this.llmService = llmService;
    }

    @GetMapping("/dynamic")
    public List<NewsItem> handleDynamicQuery(@RequestParam String query,
                                             @RequestParam(required = false) Double lat,
                                             @RequestParam(required = false) Double lon,
                                             @RequestParam(required = false) Double radius,
                                             @RequestParam(required = false) Double score) {

        LLMResponse llmResponse = llmService.extractIntentsAndEntities(query);
        List<String> intents =  llmResponse.getIntent();
        List<String> entities = llmResponse.getEntities();
        System.out.println("Intents: " + intents);
        System.out.println("entities: " + entities);

        Set<NewsItem> results = new HashSet<>();

        if (intents.isEmpty() && !entities.isEmpty()) {
            results.addAll(newsService.searchNewsByTitleAndDescription(query));
        }

        for (String intent : intents) {
            switch (intent.toLowerCase()) {
                case "category" -> {
                    System.out.println("category end point invoked..............................");
                    results.addAll(newsService.getNewsByCategory(entities));
                }
                case "source" -> {
                    System.out.println("source end point invoked.................................");
                    results.addAll(newsService.getNewsBySource(entities));
                }
                case "score" -> {
                    System.out.println("nearby end point invoked.................................");
                    double scoreThreshold = (score != null) ? score : 0.7;
                    results.addAll(newsService.getNewsByScore(scoreThreshold));
                }
                case "search" -> {
                    System.out.println("search end point invoked.................................");
                    results.addAll(newsService.searchNewsByTitleAndDescription(query));
                }
                case "nearby" -> {
                    System.out.println("nearby invoked...........................................");
                    if (lat != null && lon != null) {
                        double radiusKm = (radius != null) ? radius : 10.0;
                        results.addAll(newsService.getNewsNearby(lat, lon, radiusKm));
                    }
                }
            }
        }

        return new ArrayList<>(results);
    }
}
