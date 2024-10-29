package com.ddubok.common.auth.oauth;

import com.ddubok.common.auth.dto.GoogleResponse;
import com.ddubok.common.auth.dto.KakaoResponse;
import com.ddubok.common.auth.dto.MetaResponse;
import com.ddubok.common.auth.dto.NaverResponse;
import com.ddubok.common.auth.dto.OAuth2Response;
import com.ddubok.common.auth.exception.UnsupportedOAuth2ProviderException;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * OAuth2 인증 제공자별 응답 객체를 생성하는 팩토리 클래스. 각 소셜 로그인 제공자(Google, Naver, Kakao, Facebook)에 대한 적절한
 * OAuth2Response 구현체를 생성한다.
 */
@Component
public class OAuth2ResponseFactory {

    /**
     * OAuth2 인증 제공자 ID와 응답 속성을 기반으로 적절한 OAuth2Response 객체를 생성한다.
     * <p>
     * 지원하는 registrationId: - google: Google 로그인 - naver: 네이버 로그인 - kakao: 카카오 로그인 - facebook: 페이스북
     * 로그인
     *
     * @param registrationId OAuth2 제공자 ID (대소문자 구분 없음)
     * @param attributes     OAuth2 제공자로부터 받은 사용자 속성 Map
     * @return 제공자에 맞는 OAuth2Response 구현체
     * @throws RuntimeException 지원하지 않는 OAuth2 제공자인 경우
     */
    public OAuth2Response getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleResponse(attributes);
            case "naver" -> new NaverResponse(attributes);
            case "kakao" -> new KakaoResponse(attributes);
            case "facebook" -> new MetaResponse(attributes);
            default -> throw new UnsupportedOAuth2ProviderException(
                "해당 SNS(" + registrationId + ")는 지원하지 않습니다.");
        };
    }
}
