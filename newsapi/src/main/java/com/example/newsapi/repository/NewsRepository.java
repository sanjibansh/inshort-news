package com.example.newsapi.repository;

import com.example.newsapi.model.CategoryType;
import com.example.newsapi.model.NewsItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsItem, Long>,NewsCustomRepository {

    @Query(value = """
    SELECT DISTINCT n.*
    FROM news_item n
    JOIN news_categories nc ON n.id = nc.news_item_id
    WHERE EXISTS (
        SELECT 1
        FROM unnest(:patterns) AS pat
        WHERE LOWER(nc.category) ILIKE pat
    )
    ORDER BY n.publication_date DESC
""", nativeQuery = true)
    List<NewsItem> findByCategoryPartialMatch(@Param("patterns") String[] patterns);

    List<NewsItem> findById(UUID id);

    @Query(value = """
    SELECT * FROM news_item
    WHERE to_tsvector('english', title || ' ' || description)
          @@ websearch_to_tsquery('english', :query)
""", nativeQuery = true)
    List<NewsItem> searchNewsItemsByTitleAndDescription(@Param("query")String query);

    // Relevance score endpoint (above threshold)
    List<NewsItem> findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(double relevanceScore);

    // Search endpoint (text search)
    @Query("SELECT n FROM NewsItem n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(n.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<NewsItem> searchByQuery(@Param("query") String query);

    // Source endpoint
    List<NewsItem> findBySourceOrderByPublicationDateDesc( String source);

    // Nearby endpoint (geospatial distance-based search)
    @Query("SELECT n FROM NewsItem n WHERE " +
            "FUNCTION('earth_distance', ll_to_earth(:lat, :lon), ll_to_earth(n.latitude, n.longitude)) < :radius")
    List<NewsItem> findNearbyArticles(@Param("lat") double lat,
                                      @Param("lon") double lon,
                                      @Param("radius") double radiusInMeters);

    // Trending bonus (reuses top relevance within location)
    @Query("SELECT n FROM NewsItem n WHERE " +
            "FUNCTION('earth_distance', ll_to_earth(:lat, :lon), ll_to_earth(n.latitude, n.longitude)) < :radius " +
            "ORDER BY n.relevanceScore DESC")
    List<NewsItem> findTrendingByLocation(@Param("lat") double lat,
                                          @Param("lon") double lon,
                                          @Param("radius") double radiusInMeters);

    List<NewsItem>  findTop3BySummaryIsNull();
}

