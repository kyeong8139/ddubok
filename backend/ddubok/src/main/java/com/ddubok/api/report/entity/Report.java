package com.ddubok.api.report.entity;

import com.ddubok.api.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 신고에 대한 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    /**
     * 신고 대한 고유번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    /**
     * 신고에 대한 사유
     */
    @Column(nullable = false)
    private String content;
    /**
     * 신고에 대한 처리 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;
    /**
     * 신고 생성 일자
     */
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    /**
     * 신고 처리 일자
     */
    @Column(nullable = true)
    private LocalDateTime processedAt;
    /**
     * 신고자의 멤버 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    /**
     * 신고된 카드의 id
     */
    @Column(nullable = false)
    private Long cardId;
}
