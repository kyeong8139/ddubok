package com.ddubok.common.auth.dto;

import com.ddubok.api.member.entity.SocialProvider;
import java.util.Map;

/**
 * X OAuth2 인증 응답을 처리하는 DTO 클래스.
 * X에서 제공하는 사용자 정보를 Map 형태로 받아 처리한다.
 *
 * X OAuth2 응답 속성:
 * - id: X 사용자 고유 ID
 */
public class XResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    /**
     * X OAuth2 응답 정보로 DTO를 생성한다.
     *
     * @param attributes X에서 제공하는 사용자 속성 Map
     */
    public XResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * OAuth2 제공자 이름을 반환한다.
     *
     * @return "X" 문자열
     */
    @Override
    public String getProvider() {
        return SocialProvider.X.name();
    }

    /**
     * X 사용자의 고유 ID를 반환한다.
     * attributes Map에서 "id" 키의 값을 반환한다.
     *
     * @return X 사용자 고유 ID
     */
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

}
