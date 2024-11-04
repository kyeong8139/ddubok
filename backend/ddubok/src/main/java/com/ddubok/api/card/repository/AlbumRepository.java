package com.ddubok.api.card.repository;

import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.card.entity.Album;
import com.ddubok.api.report.entity.Report;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByCardIdAndMemberId(Long cardId, Long memberId);

    Optional<List<Album>> findByMemberId(Long memberId);

    @Query("SELECT a FROM Album a WHERE a.isDeleted = false ORDER BY a.id DESC")
    Page<Album> findAll(Pageable pageable);

    @Query("SELECT a FROM Album a WHERE a.card.season = :season AND a.isDeleted = false ORDER BY a.id DESC")
    Page<Album> findAllBySeason(Season season, Pageable pageable);
}
