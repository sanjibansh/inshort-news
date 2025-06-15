package com.example.newsapi.serviceImpl;


import com.example.newsapi.model.NewsItem;
import com.example.newsapi.repository.NewsRepository;
import com.example.newsapi.service.LLMService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LLMSummaryBackgroundWorker {

    private final NewsRepository newsRepository;

    private final LLMService llmService;

    public LLMSummaryBackgroundWorker(NewsRepository newsRepository, LLMService llmService) {
        this.newsRepository = newsRepository;
        this.llmService = llmService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void enrichSummaries() {
        List<NewsItem> items = newsRepository.findTop3BySummaryIsNull();
        items.parallelStream().forEach(newsItem -> {
            try {
                String summary = llmService.generateSummaryFromLLM(newsItem.getDescription());
                newsItem.setSummary(summary.length() > 3000 ? summary.substring(0, 3000) : summary);
                newsRepository.save(newsItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Async
    @Transactional
    public void generateSummariesAsync(List<NewsItem> items) {
        items.forEach(newsItem -> {
            try {
                String summary = llmService.generateSummaryFromLLM(newsItem.getDescription());
                newsItem.setSummary(summary.length() > 3000 ? summary.substring(0, 3000) : summary);
                newsRepository.save(newsItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
