package com.ddubok.api.member.entity;

import com.ddubok.api.member.exception.UnknownRoleException;
import com.ddubok.api.member.exception.UnknownStateException;
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
     * 알림 동의 여부
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationConsent notificationConsent;

    /**
     * 생성 시 자동 할당해주는 메서드
     */
    @PrePersist
    protected void onCreate() {
        this.notificationConsent = NotificationConsent.DISABLED;
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

    /**
     * 멤버의 역할을 수정한다.
     *
     * @param role 역할
     */
    public void updateRole(Role role) {
        if (role == null) {
            throw new UnknownRoleException("알 수 없는 상태입니다. " + role);
        }
        switch (role) {
            case ROLE_USER:
                this.role = Role.ROLE_ADMIN;
                break;
            case ROLE_ADMIN:
                this.role = Role.ROLE_USER;
                break;
            default:
                throw new UnknownRoleException("알 수 없는 역할입니다. " + role);
        }
    }

    /**
     * 멤버의 상태를 수정한다.
     *
     * @param state 상태
     */
    public void updateUserState(UserState state) {
        if (state == null) {
            throw new UnknownStateException("알 수 없는 상태입니다. " + state);
        }
        switch (state) {
            case BANNED:
                this.state = UserState.ACTIVATED;
                break;
            case ACTIVATED:
                this.state = UserState.BANNED;
                break;
            default:
                throw new UnknownStateException("알 수 없는 상태입니다. " + state);
        }
    }

    /**
     * 알림 수신에 동의한다.
     */
    public void agreeNotification() {
        this.notificationConsent = NotificationConsent.ENABLED;
    }

    /**
     * 알림 수신에 거부한다.
     */
    public void disagreeNotification() {
        this.notificationConsent = NotificationConsent.DISABLED;
    }

    /**
     * 멤버를 삭제한다.
     */
    public void deleteMember() {
        this.socialId = "INACTIVATED";
        this.state = UserState.INACTIVATED;
        this.deletedAt = LocalDateTime.now();
    }

}