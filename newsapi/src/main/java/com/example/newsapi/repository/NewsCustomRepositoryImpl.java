package com.example.newsapi.repository;

import com.example.newsapi.model.NewsItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsCustomRepositoryImpl implements NewsCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NewsItem> searchNewsItems(String query) {

        String cleanedQuery = query.toLowerCase()
                .replaceAll("\\b(search|find|show|give|me|a|an|the)\\b", "")
                .trim()
                .replaceAll("\\s+", " ");

        String sql = String.format("""
        SELECT * FROM news_item
        WHERE to_tsvector('english', title || ' ' || description)
              @@ websearch_to_tsquery('english', '%s')
        """, cleanedQuery.replace("'", "''")); // escape single quotes

        System.out.println(sql);
        return entityManager.createNativeQuery(sql, NewsItem.class)
                .getResultList();
    }



}
