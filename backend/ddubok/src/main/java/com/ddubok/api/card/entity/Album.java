package com.ddubok.api.card.entity;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "album")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {

    /**
     * 앨범 정보 고유 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;
    /**
     * 카드를 받는 사용자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    /**
     * 카드의 고유 id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    /**
     * 카드의 삭제 여부
     */
    @Column(nullable = false)
    private Boolean isDeleted;
    /**
     * 사용자가 카드의 내용을 읽었는지 여부
     */
    @Column(nullable = false)
    private Boolean isRead;

    /**
     * 앨범 객체를 생성하는 생성자
     *
     * @param member 카드를 소유한 member
     * @param card   소유할 카드 정보
     */
    @Builder
    public Album(Member member, Card card) {
        this.member = member;
        this.card = card;
        this.isDeleted = false;
        this.isRead = false;
    }

    /**
     * 앨범에서 카드를 삭제하는 메서드
     */
    public void delete() {
        this.isDeleted = true;
    }

    /**
     * 카드 content를 읽음 상태로 업데이트 하는 메서드
     */
    public void read() {
        this.isRead = true;
    }
}
