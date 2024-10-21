package com.ddubok.api.card.entity;

import com.ddubok.api.admin.entity.Season;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카드 정보에 관련된 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    /**
     * 카드 정보 고유 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    /**
     * 카드를 받는 사용자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;
    /**
     * 카드의 내용
     */
    @Column(nullable = false)
    private String content;
    /**
     * 카드 생성 일자
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
    /**
     * 카드가 사용자에게 오픈될 예정일
     */
    @Column(nullable = false)
    private LocalDateTime openedAt;
    /**
     * 카드 이미지 경로 url
     */
    @Column(nullable = false)
    private String path;
    /**
     * 카드의 상태 <br> READY(오픈예정), OPEN(오픈됨), FILTERED(내용 필터링됨), DELETE(삭제됨)
     */
    @Column(nullable = false)
    private State state;
    /**
     * 카드를 보낸이의 닉네임
     */
    @Column(nullable = true)
    private String writerName;
    /**
     * 카드가 속할 시즌의 고유 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;
}