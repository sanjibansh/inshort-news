
package com.example.newsapi.controller.v1;

import com.example.newsapi.dto.NewsItemDTO;
import com.example.newsapi.service.TrendingNewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class TrendingController {

    private final TrendingNewsService trendingNewsService;

    public TrendingController(TrendingNewsService trendingNewsService) {
        this.trendingNewsService = trendingNewsService;
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingNews(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            List<NewsItemDTO> trendingNews = trendingNewsService.getTrendingNews(lat, lon, limit);

            if (trendingNews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No trending news found for this location.");
            }

            return ResponseEntity.ok(trendingNews);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching trending news. " + ex);
        }
    }
}
