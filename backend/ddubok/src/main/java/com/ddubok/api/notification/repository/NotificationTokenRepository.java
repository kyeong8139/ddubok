package com.ddubok.api.notification.repository;

import com.ddubok.api.notification.entity.NotificationToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

    @Query(value = "DELETE FROM notification_token WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberId(@Param("memberId") Long memberId);
}
