package com.ddubok.common.auth.oauth;

import com.ddubok.common.auth.dto.GoogleResponse;
import com.ddubok.common.auth.dto.KakaoResponse;
import com.ddubok.common.auth.dto.MetaResponse;
import com.ddubok.common.auth.dto.NaverResponse;
import com.ddubok.common.auth.dto.OAuth2Response;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ResponseFactory {

    public OAuth2Response getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleResponse(attributes);
            case "naver" -> new NaverResponse(attributes);
            case "kakao" -> new KakaoResponse(attributes);
            case "facebook" -> new MetaResponse(attributes);
            default -> throw new RuntimeException(
                "Sorry! Login with " + registrationId + " is not supported yet.");
        };
    }
}
