package com.ddubok.api.coin.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 코인 정보에 관련된 엔티티 클래스
 */
@Getter
@Entity
@Table(name = "coin")
@NoArgsConstructor
public class Coin {

    /**
     * 코인 대한 고유번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinId;
    /**
     * 코인에 대한 잔고
     */
    @Column(nullable = false)
    private Integer balance;
    /**
     * 신고자의 멤버 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;
}
