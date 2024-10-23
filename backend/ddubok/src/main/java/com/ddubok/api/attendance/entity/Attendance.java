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
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 날짜별 출석 정보에 관련된 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    /**
     * 출석 정보 고유 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;
    /**
     * 출석자의 멤버 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    /**
     * 출석 일자 및 시간
     */
    @Column(nullable = false)
    private LocalDate date;

    @Builder
    public Attendance(Member member) {
        this.member = member;
        this.date = LocalDate.now();
    }
}
