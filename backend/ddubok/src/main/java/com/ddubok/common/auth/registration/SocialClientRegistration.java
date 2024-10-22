package com.ddubok.common.auth.registration;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Component;

@Component
public class SocialClientRegistration {

    public ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
            .clientId("6a7ddc490e7bc00b00cc66c5241048ed")
            .redirectUri("http://localhost:8080/api/login/oauth2/code/kakao")
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
            .clientId("pB4sVsA3vzTtTc85_65u")
            .clientSecret("byi9Wst3A_")
            .redirectUri("http://localhost:8080/api/login/oauth2/code/naver")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope()
            .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
            .tokenUri("https://nid.naver.com/oauth2.0/token")
            .userInfoUri("https://openapi.naver.com/v1/nid/me")
            .userNameAttributeName("response")
            .build();
    }

    public ClientRegistration metaClientRegistration() {
        return ClientRegistration.withRegistrationId("facebook")
            .clientId("1198754194560080")
            .clientSecret("7788fda0c1b01f0aa711f6b24743285c")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:8080/api/login/oauth2/code/facebook")
            .scope()
            .authorizationUri("https://www.facebook.com/v18.0/dialog/oauth")
            .tokenUri("https://graph.facebook.com/v18.0/oauth/access_token")
            .userInfoUri("https://graph.facebook.com/v18.0/me?fields=id,name,email,picture")
            .userNameAttributeName("id")
            .build();
    }

    public ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
            .clientId("1052542036504-i8etlisvr1r6g3iah11vo4dpc6qgn8l7.apps.googleusercontent.com")
            .clientSecret("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")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:8080/api/login/oauth2/code/google")
            .scope("openid")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://www.googleapis.com/oauth2/v4/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .build();
    }
}
