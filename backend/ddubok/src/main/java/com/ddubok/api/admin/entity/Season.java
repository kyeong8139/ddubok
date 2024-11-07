package com.ddubok.api.admin.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시즌에 대한 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "season")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season {

    /**
     * 시즌 대한 고유번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    private Long id;
    /**
     * 시즌의 이름
     */
    @Column(nullable = false)
    private String name;
    /**
     * 시즌의 설명
     */
    @Column(nullable = false)
    private String description;
    /**
     * 메인화면에 나타날 배너 이미지 path
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "season_paths", joinColumns = @jakarta.persistence.JoinColumn(name = "season_id"))
    @Column(nullable = false)
    private List<String> path;
    /**
     * 시즌의 시작 일자
     */
    @Column(nullable = false)
    private LocalDateTime startedAt;
    /**
     * 시즌의 종료 일자
     */
    @Column(nullable = false)
    private LocalDateTime endedAt;
    /**
     * 시즌내에서 카드 오픈 일자
     */
    @Column(nullable = true)
    private LocalDateTime openedAt;


    /**
     * 시즌을 생성하는 생성자
     *
     * @param name 시즌의 이름
     * @param description 시즌의 설명
     * @param path 배너의 이미지 경로
     * @param startedAt 시즌 시작날짜
     * @param endedAt 시즌 종료날짜
     * @param openedAt 카드 오픈날짜
     */
    @Builder
    public Season(String name, String description, List<String> path, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime openedAt) {
        this.name = name;
        this.description = description;
        this.path = path;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.openedAt = openedAt;
    }
}
