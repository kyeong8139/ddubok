package com.ddubok.common.auth.registration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * OAuth2 클라이언트 등록 정보를 저장소에 구성하는 설정 클래스. 네이버, 카카오, 구글, 메타의 클라이언트 등록 정보를 메모리에 저장한다.
 */
@Configuration
public class SocialClientRegistrationConfig {

    private final SocialClientRegistration socialClientRegistration;

    /**
     * 소셜 로그인 클라이언트 등록 정보를 주입받는 생성자.
     *
     * @param socialClientRegistration 소셜 로그인 클라이언트 등록 정보
     */
    public SocialClientRegistrationConfig(SocialClientRegistration socialClientRegistration) {
        this.socialClientRegistration = socialClientRegistration;
    }

    /**
     * OAuth2 클라이언트 등록 저장소를 구성한다. 모든 소셜 로그인 제공자의 등록 정보를 인메모리 저장소에 저장한다.
     *
     * @return 인메모리 클라이언트 등록 저장소
     */
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
            socialClientRegistration.naverClientRegistration(),
            socialClientRegistration.kakaoClientRegistration(),
            socialClientRegistration.googleClientRegistration(),
            socialClientRegistration.metaClientRegistration());
    }
}
