package com.ddubok.common.config;

import com.ddubok.common.auth.oauth.CustomOAuth2SuccessHandler;
import com.ddubok.common.auth.oauth.CustomOAuth2UserService;
import com.ddubok.common.auth.oauth.CustomOidcUserService;
import com.ddubok.common.auth.registration.SocialClientRegistrationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final SocialClientRegistrationConfig socialClientRegistrationConfig;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        http
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            );

        http
            .oauth2Login((oauth2) -> oauth2
                .clientRegistrationRepository(socialClientRegistrationConfig.clientRegistrationRepository())
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService))
                .authorizationEndpoint(endPoint -> endPoint.baseUri("/api/oauth2/authorization"))
                .redirectionEndpoint(endPoint -> endPoint.baseUri("/api/login/oauth2/code/*"))
                .successHandler(customOAuth2SuccessHandler));

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/logout", "/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );

        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
