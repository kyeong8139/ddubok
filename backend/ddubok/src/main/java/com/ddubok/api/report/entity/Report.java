package com.ddubok.api.report.entity;

import com.ddubok.api.card.entity.Card;
import com.ddubok.api.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
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
    @Column(name = "report_id")
    private Long id;
    /**
     * 신고에 대한 제목
     */
    @Column(nullable = false)
    private String title;
    /**
     * 신고에 대한 사유
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;
    /**
     * 신고에 대한 내용
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    /**
     * 신고 엔티티를 생성하는 빌더. 신고 내용, 신고자, 카드 ID를 포함하여 신고 객체를 생성하며, 신고 상태는 기본값으로 UNPROCESSED로 설정됩니다.
     *
     * @param title   신고 제목
     * @param content 신고 내용 또는 사유
     * @param member  신고한 멤버 (신고자)
     * @param card    신고된 카드
     */
    @Builder
    public Report(String title, ReportType reportType, String content, Member member, Card card) {
        this.title = title;
        this.reportType = reportType;
        this.content = content;
        this.member = member;
        this.card = card;
        this.state = State.UNPROCESSED;
    }

    /**
     * 신고의 상태를 수정한다.
     *
     * @param state 닉네임
     */
    public void updateReportState(State state) {
        this.state = state;
        this.processedAt = LocalDateTime.now();
    }
}
