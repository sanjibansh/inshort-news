package com.example.newsapi.serviceImpl;

import com.example.newsapi.model.NewsItem;
import com.example.newsapi.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NewsBulkLoaderServiceImpl {

    private static final int BATCH_SIZE = 1000;

    private final NewsRepository newsRepository;
    private final ObjectMapper objectMapper;
    private final LLMSummaryBackgroundWorker summaryWorker;

    public NewsBulkLoaderServiceImpl(NewsRepository newsRepository, LLMSummaryBackgroundWorker summaryWorker) {
        this.newsRepository = newsRepository;
        this.summaryWorker = summaryWorker;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<NewsItem> loadNewsFromJson(Path jsonFilePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(jsonFilePath)) {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser parser = jsonFactory.createParser(inputStream);

            List<NewsItem> batch = new ArrayList<>();
            List<NewsItem> allSavedItems = new ArrayList<>();  // Collect all saved news items

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected START_ARRAY token");
            }

            while (parser.nextToken() == JsonToken.START_OBJECT) {
                NewsItem newsItem = objectMapper.readValue(parser, NewsItem.class);
                batch.add(newsItem);

                if (batch.size() >= BATCH_SIZE) {
                    newsRepository.saveAll(batch);
                    allSavedItems.addAll(batch);
                    summaryWorker.generateSummariesAsync(batch); // async summary
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                newsRepository.saveAll(batch);
                allSavedItems.addAll(batch);
                summaryWorker.generateSummariesAsync(batch);
            }

            return allSavedItems;
        }
    }

    public void seedUserEvents(List<NewsItem> loadedNews, int i) {}
}
