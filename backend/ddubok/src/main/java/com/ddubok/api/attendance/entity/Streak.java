package com.ddubok.api.attendance.entity;

import com.ddubok.api.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 출석 스트릭 정보에 관련된 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "streak")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Streak {

    /**
     * 출석 스트릭 정보 고유 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streak_id")
    private Long id;
    /**
     * 현재 출석 스트릭 일자
     */
    @Column(nullable = false)
    private Integer currentStreak;
    /**
     * 이번달 최장 출석 스트릭 일자
     */
    @Column(nullable = false)
    private Integer maxStreak;
    /**
     * 출석 스트릭 변경 일자
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    /**
     * 출석자의 멤버 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    public Streak(int currentStreak, int maxStreak, Member member) {
        this.currentStreak = currentStreak;
        this.maxStreak = maxStreak;
        this.member = member;
    }

    public void resetCurrentStreak() {
        this.currentStreak = 1;
    }

    public void resetMaxStreak() {
        this.maxStreak = 1;
    }

    public void addCurrentStreak() {
        this.currentStreak = this.currentStreak + 1;
    }

    public void syncMaxStreakWithCurrent() {
        this.maxStreak = this.currentStreak;
    }
}
