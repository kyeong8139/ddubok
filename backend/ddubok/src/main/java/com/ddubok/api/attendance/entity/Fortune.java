package com.ddubok.api.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 운세를 저장하고 관리하는 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "fortune")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Fortune {

    /**
     * 운세 정보 고유 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fortune_id")
    private Long id;
    /**
     * 운세의 내용
     */
    @Column(nullable = false)
    private String sentence;
}
