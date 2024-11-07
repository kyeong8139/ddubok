package com.ddubok.api.card.entity;

import com.ddubok.api.admin.entity.Season;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(name = "card_id")
    private Long id;
    /**
     * 카드의 내용
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    /**
     * 카드 생성 일자
     */
    @CreationTimestamp
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
    @Column(nullable = false, columnDefinition = "TEXT")
    private String path;
    /**
     * 카드의 상태 <br> READY(오픈예정), OPEN(오픈됨), FILTERED(내용 필터링됨)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;
    /**
     * 카드를 보낸이의 닉네임
     */
    @Column(nullable = false, length = 11)
    private String writerName;
    /**
     * 카드가 속할 시즌의 고유 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    /**
     * 카드 객체를 생성하는 생성자
     * <p>
     * 기본 생성자이며, 카드의 오픈일자를 시즌의 카드 오픈일자로 지정하고, 상태를 {@code State.READY}로 설정
     * </p>
     *
     * @param content    카드의 내용
     * @param path       카드 이미지 url
     * @param writerName 카드를 보낸 사람 닉네임
     * @param season     카드의 시즌 정보
     */
    @Builder
    public Card(String content, String path, String writerName,
        Season season) {
        this(content, season.getOpenedAt(), path, State.READY, writerName,
            season);
    }

    /**
     * 카드 객체를 생성하는 프라이빗 생성자
     * <p>
     * 모든 필드를 초기화
     * </p>
     *
     * @param content    카드의 내용
     * @param openedAt   카드가 오픈될 예정일
     * @param path       카드 이미지 url
     * @param state      카드의 상태
     * @param writerName 카드를 보낸 사람 닉네임
     * @param season     카드의 시즌 정보
     */
    private Card(String content, LocalDateTime openedAt, String path, State state,
        String writerName, Season season) {
        this.content = content;
        this.openedAt = openedAt;
        this.path = path;
        this.state = state;
        this.writerName = writerName;
        this.season = season;
    }

    public void filtering() {
        this.state = State.FILTERED;
    }
}