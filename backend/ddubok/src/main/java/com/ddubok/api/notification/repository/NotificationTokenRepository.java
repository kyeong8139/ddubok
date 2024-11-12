package com.ddubok.api.notification.repository;

import com.ddubok.api.notification.entity.NotificationToken;
import io.lettuce.core.dynamic.annotation.Param;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

    @Query(value = "DELETE FROM notification_token WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberId(@Param("memberId") Long memberId);

    List<NotificationToken> findAllByMemberId(Long memberId);

    @Query("SELECT nt.token FROM NotificationToken nt WHERE nt.member.notificationConsent = com.ddubok.api.member.entity.NotificationConsent.ENABLED")
    List<String> findTokensByEnabledNotification();

    @Query("SELECT nt FROM NotificationToken nt JOIN nt.member m JOIN Album a ON a.member = m WHERE a.card.id = :cardId")
    List<NotificationToken> findNotificationTokensByCardId(@Param("cardId") Long cardId);

    @Query("SELECT DISTINCT nt FROM NotificationToken nt JOIN nt.member m JOIN Album a ON a.member = m JOIN a.card c WHERE c.season.id = :seasonId ")
    List<NotificationToken> findNotificationTokensBySeasonId(@Param("seasonId") Long seasonId);
}
