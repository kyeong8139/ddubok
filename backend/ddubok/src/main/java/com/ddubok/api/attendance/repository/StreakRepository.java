package com.ddubok.api.attendance.repository;

import com.ddubok.api.attendance.entity.Streak;
import com.ddubok.api.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StreakRepository extends JpaRepository<Streak, Long> {

    @Query("SELECT s FROM Streak s WHERE s.member.id = :memberId")
    Streak findByMemberId(Long memberId);

    @Query("SELECT s FROM Streak s WHERE s.member = :member")
    Optional<Streak> findByMember(Member member);
}