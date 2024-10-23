package com.ddubok.common.auth.dto;

import com.ddubok.api.member.entity.SocialProvider;
import java.util.Map;

/**
 * Naver OAuth2 인증 응답을 처리하는 DTO 클래스.
 * Naver에서 제공하는 사용자 정보를 Map 형태로 받아 처리한다.
 *
 * Naver OAuth2 응답 속성:
 * - id: Naver 사용자 고유 ID
 */
public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    /**
     * Naver OAuth2 응답 정보로 DTO를 생성한다.
     *
     * @param attributes Naver에서 제공하는 사용자 속성 Map
     */
    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * OAuth2 제공자 이름을 반환한다.
     *
     * @return "NAVER" 문자열
     */
    @Override
    public String getProvider() {
        return SocialProvider.NAVER.name();
    }

    /**
     * Naver 사용자의 고유 ID를 반환한다.
     * attributes Map의 response Map에서 "id" 키의 값을 반환한다.
     *
     * @return Naver 사용자 고유 ID
     */
    @Override
    public String getProviderId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return response.get("id").toString();
    }
}
