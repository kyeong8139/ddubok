package com.ddubok.api.card.repository;

import com.ddubok.api.card.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c, r FROM Card c LEFT JOIN Report r ON c.id = r.card.id")
    List<Object[]> findAllCardsWithReports();
}
