package com.ddubok.common.auth.dto;

/**
 * OAuth2 인증 제공자의 응답을 표준화하는 인터페이스.
 * 각 소셜 로그인 제공자(Google, Naver, Kakao, X 등)의 응답 DTO가 구현한다.
 * 서로 다른 제공자들의 응답 형식을 일관된 방식으로 처리하기 위해 사용된다.
 */
public interface OAuth2Response {

    /**
     * OAuth2 인증 제공자의 이름을 반환한다.
     * 예: "GOOGLE", "NAVER", "KAKAO", "X"
     *
     * @return 제공자 이름 (대문자)
     */
    String getProvider();

    /**
     * OAuth2 제공자가 제공하는 사용자의 고유 식별자를 반환한다.
     * 각 제공자별 식별자 형식:
     * - Google: sub 값 (예: "12345678")
     * - Naver: id 값
     * - Kakao: id 값
     * - X: id 값
     * @return 제공자별 사용자 고유 ID
     */
    String getProviderId();
}
