package com.ddubok.common.auth.registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Component;

/**
 * 소셜 로그인 제공자들의 OAuth2 클라이언트 등록 정보를 관리하는 클래스. application.yml의 spring.social 설정값을 주입받는다.
 * <p>
 * 지원하는 소셜 로그인: - 카카오 - 네이버 - 메타 - 구글
 */
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.social")
public class SocialClientRegistration {

    private String kakaoId;
    private String naverId;
    private String naverSecret;
    private String xId;
    private String xSecret;
    private String googleId;
    private String googleSecret;

    public ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
            .clientId(kakaoId)
            .redirectUri("https://ddubok.com/api/login/oauth2/code/kakao")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope()
            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
            .tokenUri("https://kauth.kakao.com/oauth/token")
            .userInfoUri("https://kapi.kakao.com/v2/user/me")
            .userNameAttributeName("id")
            .build();
    }

    public ClientRegistration naverClientRegistration() {
        return ClientRegistration.withRegistrationId("naver")
            .clientId(naverId)
            .clientSecret(naverSecret)
            .redirectUri("https://ddubok.com/api/login/oauth2/code/naver")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope()
            .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
            .tokenUri("https://nid.naver.com/oauth2.0/token")
            .userInfoUri("https://openapi.naver.com/v1/nid/me")
            .userNameAttributeName("response")
            .build();
    }

    public ClientRegistration metaClientRegistration() {
        return ClientRegistration.withRegistrationId("x")
            .clientId(xId)
            .clientSecret(xSecret)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("https://ddubok.com/api/login/oauth2/code/x")
            .scope()
            .authorizationUri("https://twitter.com/i/oauth2/authorize")
            .tokenUri("https://api.twitter.com/2/oauth2/token")
            .userInfoUri("https://api.twitter.com/2/users/me")
            .userNameAttributeName("data")
            .build();
    }

    public ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
            .clientId(googleId)
            .clientSecret(googleSecret)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("https://ddubok.com/api/login/oauth2/code/google")
            .scope("openid")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://www.googleapis.com/oauth2/v4/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .build();
    }
}
