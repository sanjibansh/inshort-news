package com.example.newsapi.serviceImpl;

import com.example.newsapi.dto.LLMResponse;
import com.example.newsapi.service.LLMService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class LLMServiceImpl implements LLMService {

    private final OpenAIService openAIService;

    public LLMServiceImpl(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @Override
    public LLMResponse extractIntentsAndEntities(String userQuery) {
        String prompt = """
        You are an information extraction model. Your job is to extract intent and named entities from user queries.
        
        Return output as a **valid JSON object**, containing two keys in small letters:
        - `intent`: a list of intent categories such as ["category", "source", "nearby"]
        - `entities`: a list of named entities mentioned in the query
        
        Examples:
        
        User query: "Top technology news from the New York Times"
        Output:
        {
          "intent": ["category", "source"],
          "entities": ["Technology", "New York Times"]
        }
        
        User query: "%s"
        Output:
        """.formatted(userQuery);

        String response = openAIService.getChatCompletion(prompt);
        return parseJson(response);
    }

    private LLMResponse parseJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

            if (contentNode.isMissingNode()) {
                throw new RuntimeException("Missing content in LLM response");
            }

            String innerJson = contentNode.asText();
            return mapper.readValue(innerJson, LLMResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON from LLM response", e);
        }
    }

    @Override
    public String generateSummaryFromLLM(String newsDescription){
        if (newsDescription == null || newsDescription.isEmpty()) {
            return "No content available for summary.";
        }

        String prompt = """
        Summarize the following news article in concise sentences, highlighting the main points clearly and objectively:
        
        "%s"
        """.formatted(newsDescription);

        String llmRootResponse = openAIService.getChatCompletion(prompt);
        return parseToSummaryString(llmRootResponse);
    }

    public String parseToSummaryString(String llmRootResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(llmRootResponse);
            JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

            if (!contentNode.isMissingNode()) {
                String summary = contentNode.asText().trim();
                System.out.println("LLM Summary received...... ");
                return summary;

            } else {
                return "Summary not available.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing summary.";
        }
    }
}
