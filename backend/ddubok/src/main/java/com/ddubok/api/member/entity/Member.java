package com.ddubok.api.member.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "member")
public class Member {

    /**
     * 멤버 고유 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
     * 멤버 역할
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * 가입 SNS
     */
    @Column(nullable = false)
    private String socialProvider;

    /**
     * SNS 제공 고유 ID
     */
    @Column(nullable = false)
    private String socialId;

    /**
     * 멤버 닉네임
     */
    @Column(nullable = false)
    private String nickname;

    /**
     * 멤버 상태 <br> ACTIVATED(활성), INACTIVATED(비활성), BANNED(차단)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserState state;

    /**
     * 가입 날짜
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 수정 날짜
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 탈퇴 날짜
     */
    @Column
    private LocalDateTime deletedAt;

    /**
     * 생성 시 자동 할당해주는 메서드
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.state = UserState.ACTIVATED;
    }

    /**
     * 업데이트 시 자동 할당해주는 메서드
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 멤버 닉네임을 수정한다.
     *
     * @param nickname 닉네임
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void deleteMember() {
        this.socialId = "INACTIVATED";
        this.state = UserState.INACTIVATED;
        this.deletedAt = LocalDateTime.now();
    }

}