//package com.example.newsapi.controller.v1;
//
//import com.example.newsapi.dto.LLMResponse;
//import com.example.newsapi.model.NewsItem;
//import com.example.newsapi.service.LLMService;
//import com.example.newsapi.service.NewsService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.*;
//
//@RestController
//@RequestMapping("api/v1/news")
//public class NewsController {
//
//    private final NewsService newsService;
//    private final LLMService llmService;
//
//    public NewsController(NewsService newsService, LLMService llmService) {
//        this.newsService = newsService;
//        this.llmService = llmService;
//    }
//
//    @GetMapping("/dynamic")
//    public List<NewsItem> handleDynamicQuery(@RequestParam String query,
//                                             @RequestParam(required = false) Double lat,
//                                             @RequestParam(required = false) Double lon,
//                                             @RequestParam(required = false) Double radius,
//                                             @RequestParam(required = false) Double score) {
//
//        LLMResponse llmResponse = llmService.extractIntentsAndEntities(query);
//        List<String> intents =  llmResponse.getIntent();
//        List<String> entities = llmResponse.getEntities();
//        System.out.println("Intents: " + intents);
//        System.out.println("entities: " + entities);
//
//        Set<NewsItem> results = new HashSet<>();
//
//        if (intents.isEmpty() && !entities.isEmpty()) {
//            results.addAll(newsService.searchNewsByTitleAndDescription(query));
//        }
//
//        for (String intent : intents) {
//            switch (intent.toLowerCase()) {
//                case "category" -> {
//                    System.out.println("category end point invoked..............................");
//                    results.addAll(newsService.getNewsByCategory(entities));
//                }
//                case "source" -> {
//                    System.out.println("source end point invoked.................................");
//                    results.addAll(newsService.getNewsBySource(entities));
//                }
//                case "score" -> {
//                    System.out.println("nearby end point invoked.................................");
//                    double scoreThreshold = (score != null) ? score : 0.7;
//                    results.addAll(newsService.getNewsByScore(scoreThreshold));
//                }
//                case "search" -> {
//                    System.out.println("search end point invoked.................................");
//                    results.addAll(newsService.searchNewsByTitleAndDescription(query));
//                }
//                case "nearby" -> {
//                    System.out.println("nearby invoked...........................................");
//                    if (lat != null && lon != null) {
//                        double radiusKm = (radius != null) ? radius : 10.0;
//                        results.addAll(newsService.getNewsNearby(lat, lon, radiusKm));
//                    }
//                }
//            }
//        }
//
//        return new ArrayList<>(results);
//    }
//}







//package com.example.newsapi.serviceImpl;
//
//
//import com.example.newsapi.model.NewsItem;
//import com.example.newsapi.repository.NewsRepository;
//import com.example.newsapi.service.NewsService;
//import com.example.newsapi.util.DistanceCalculator;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class NewsServiceImpl implements NewsService {
//
//    private final NewsRepository newsRepository;
//    private final DistanceCalculator distanceCalculator;
//
//
//    public NewsServiceImpl(NewsRepository newsRepository, DistanceCalculator distanceCalculator) {
//        this.newsRepository = newsRepository;
//        this.distanceCalculator = distanceCalculator;
//    }
//    @Primary
//    @Override
//    public List<NewsItem> searchNewsByTitleAndDescription(String query) {
//        List<NewsItem> searchResult = newsRepository.searchNewsItems(query);
//        System.out.println("After returning newsCustomRepoImpl........" + searchResult);
//        return searchResult;
//    }
//
//    @Override
//    public List<NewsItem> getNewsByCategory(List<String> categories) {
//        // return newsRepository.findByCategoryOrderByPublishedAtDesc(categories);
//        String[] patterns = categories.stream()
//                .map(cat -> "%" + cat.toLowerCase() + "%") // for partial match
//                .toArray(String[]::new);
//
//        return newsRepository.findByCategoryPartialMatch(patterns);
//    }
//
//    @Override
//    public List<NewsItem> getNewsBySource(List<String> sources) {
//        List<NewsItem> ListOfAllNewsItem = new ArrayList<>();
//
//        for(String source: sources) {
//        List<NewsItem> listOfNewsItem = newsRepository.findBySourceOrderByPublicationDateDesc(source);
//        ListOfAllNewsItem.addAll(listOfNewsItem);
//        }
//        return ListOfAllNewsItem;
//    }
//
//    @Override
//    public List<NewsItem> getNewsByScore(double threshold) {
//        return newsRepository.findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(threshold);
//    }
//
//    @Override
//    public List<NewsItem> getNewsBySearchQuery(String query) {
//        System.out.println("return by postgressssssssssssssssssssssssssssssssssssssssssssss");
//
//
//        String formattedQuery = Arrays.stream(query.trim().split("\\s+"))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .collect(Collectors.joining(" & "));  // -> "search & musk & news"
//
//        return newsRepository.searchNewsItemsByTitleAndDescription(query);
//    }
//
//    @Override
//    public List<NewsItem> getNewsNearby(double latitude, double longitude, double radiusInKm) {
//        List<NewsItem> allNews = newsRepository.findAll();
//        return allNews.stream()
//                .filter(news -> {
//                    double distance = DistanceCalculator.calculateDistance(latitude, longitude,
//                            news.getLatitude(), news.getLongitude());
//                    return distance <= radiusInKm;
//                })
//                .sorted((a, b) -> {
//                    double distA = DistanceCalculator.calculateDistance(latitude, longitude,
//                            a.getLatitude(), a.getLongitude());
//                    double distB = DistanceCalculator.calculateDistance(latitude, longitude,
//                            b.getLatitude(), b.getLongitude());
//                    return Double.compare(distA, distB);
//                })
//                .toList();
//    }
//
//    @Override
//    public List<NewsItem> getTrendingNewsNearby(double latitude, double longitude, double radiusInKm) {
//        // For simplicity, treat trending as top 10 by relevance score within radius.
//        return getNewsNearby(latitude, longitude, radiusInKm).stream()
//                .sorted((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()))
//                .limit(10)
//                .toList();
//    }
//
//}
//










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
