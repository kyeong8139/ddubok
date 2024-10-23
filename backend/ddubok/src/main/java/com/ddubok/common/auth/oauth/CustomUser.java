package com.ddubok.common.auth.oauth;

/**
 * OAuth2 인증된 사용자의 공통 인터페이스.
 * CustomOAuth2User와 CustomOidcUser 클래스에서 구현하여 사용한다.
 */
public interface CustomUser {

    /**
     * 사용자의 고유 ID를 반환한다.
     * 이 ID는 시스템 내부에서 사용되는 식별자이다.
     *
     * @return 사용자의 고유 ID
     */
    Long getId();

    /**
     * 사용자의 권한 역할을 반환한다.
     * 예: "ROLE_USER", "ROLE_ADMIN"
     *
     * @return 사용자 역할 문자열
     */
    String getRole();
}
