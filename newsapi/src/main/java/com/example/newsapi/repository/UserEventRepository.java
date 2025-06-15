package com.example.newsapi.repository;

import com.example.newsapi.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, UUID> {

    // Find all events after a given timestamp
    List<UserEvent> findByTimestampAfter(LocalDateTime timestamp);
}