package com.example.newsapi.service;

import com.example.newsapi.dto.LLMResponse;

public interface LLMService {
    LLMResponse extractIntentsAndEntities(String userQuery);
    String generateSummaryFromLLM(String newsDescription);

}
