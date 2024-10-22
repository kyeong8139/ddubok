package com.ddubok.api.card.repository;

import com.ddubok.api.card.entity.Album;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByCardIdAndMemberId(Long cardId, Long memberId);
}
