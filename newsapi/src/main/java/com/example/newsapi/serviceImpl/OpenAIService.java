package com.example.newsapi.serviceImpl;

import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenAIService {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    private final String API_KEY = "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"; // openAiApiKey;
    private final OkHttpClient client = new OkHttpClient();

    public String getChatCompletion(String prompt) {
        // Create user message JSON
        JSONObject userMsg = new JSONObject()
                .put("role", "user")
                .put("content", prompt);

        JSONArray messages = new JSONArray().put(userMsg);

        // Payload for OpenAI
        JSONObject payload = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", messages);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(payload.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error " + response.code() + ": " + response.body().string());
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("OpenAI call failed", e);
        }
    }
}
