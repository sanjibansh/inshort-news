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

import com.example.newsapi.model.NewsItem;
import com.example.newsapi.repository.NewsRepository;
import com.example.newsapi.service.NewsService;
import com.example.newsapi.util.DistanceCalculator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final DistanceCalculator distanceCalculator;

    public NewsServiceImpl(NewsRepository newsRepository, DistanceCalculator distanceCalculator) {
        this.newsRepository = newsRepository;
        this.distanceCalculator = distanceCalculator;
    }

    @Primary
    @Override
    public List<NewsItem> searchNewsByTitleAndDescription(String query) {
        try {
            return newsRepository.searchNewsItems(query);
        } catch (Exception e) {
            System.err.println("Error in searchNewsByTitleAndDescription: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<NewsItem> getNewsByCategory(List<String> categories) {
        try {
            String[] patterns = categories.stream()
                    .map(cat -> "%" + cat.toLowerCase() + "%")
                    .toArray(String[]::new);
            return newsRepository.findByCategoryPartialMatch(patterns);
        } catch (Exception e) {
            System.err.println("Error in getNewsByCategory: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<NewsItem> getNewsBySource(List<String> sources) {
        try {
            List<NewsItem> all = new ArrayList<>();
            for (String source : sources) {
                all.addAll(newsRepository.findBySourceOrderByPublicationDateDesc(source));
            }
            return all;
        } catch (Exception e) {
            System.err.println("Error in getNewsBySource: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<NewsItem> getNewsByScore(double threshold) {
        try {
            return newsRepository.findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(threshold);
        } catch (Exception e) {
            System.err.println("Error in getNewsByScore: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<NewsItem> getNewsBySearchQuery(String query) {
        try {
            String formattedQuery = Arrays.stream(query.trim().split("\\s+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(" & "));
            return newsRepository.searchNewsItemsByTitleAndDescription(formattedQuery);
        } catch (Exception e) {
            System.err.println("Error in getNewsBySearchQuery: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<NewsItem> getNewsNearby(double latitude, double longitude, double radiusInKm) {
        try {
            List<NewsItem> allNews = newsRepository.findAll();
            return allNews.stream()
                    .filter(news -> DistanceCalculator.calculateDistance(latitude, longitude,
                            news.getLatitude(), news.getLongitude()) <= radiusInKm)
                    .sorted(Comparator.comparingDouble(n ->
                            DistanceCalculator.calculateDistance(latitude, longitude, n.getLatitude(), n.getLongitude())))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error in getNewsNearby: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<NewsItem> getTrendingNewsNearby(double latitude, double longitude, double radiusInKm) {
        try {
            return getNewsNearby(latitude, longitude, radiusInKm).stream()
                    .sorted((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()))
                    .limit(10)
                    .toList();
        } catch (Exception e) {
            System.err.println("Error in getTrendingNewsNearby: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
