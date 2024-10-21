package com.ddubok.common.auth.registration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class SocialClientRegistrationConfig {

    private final SocialClientRegistration socialClientRegistration;

    public SocialClientRegistrationConfig(SocialClientRegistration socialClientRegistration) {
        this.socialClientRegistration = socialClientRegistration;
    }

    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
            socialClientRegistration.naverClientRegistration(),
            socialClientRegistration.kakaoClientRegistration(),
            socialClientRegistration.googleClientRegistration(),
            socialClientRegistration.metaClientRegistration());
    }
}
