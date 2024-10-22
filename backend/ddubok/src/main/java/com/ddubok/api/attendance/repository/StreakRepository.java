package com.ddubok.api.attendance.repository;

import com.ddubok.api.attendance.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreakRepository extends JpaRepository<Streak, Long> {

    Streak findByMemberId(Long memberId);
}