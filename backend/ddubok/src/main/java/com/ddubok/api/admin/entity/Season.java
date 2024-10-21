package com.ddubok.api.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시즌에 대한 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season {

    /**
     * 시즌 대한 고유번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seasonId;
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
    @Column(nullable = false)
    private String path;
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
}
