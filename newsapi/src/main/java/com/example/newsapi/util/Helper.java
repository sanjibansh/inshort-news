package com.example.newsapi.util;

import java.util.List;

public class Helper {
    public static List<String> sanitizeCategory(List<String> category) {
        return category.stream()
                .flatMap(c -> List.of(c.split("category=")).stream())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
    }
}

