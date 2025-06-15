package com.example.newsapi.dto;

import java.util.List;

public class LLMResponse {
    private List<String> intent;
    private List<String> entities;

    public List<String> getIntent() {
        return intent;
    }

    public void setIntent(List<String> intent) {
        this.intent = intent;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }
}

