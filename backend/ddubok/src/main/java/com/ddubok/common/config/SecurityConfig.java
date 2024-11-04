package com.ddubok.common.config;

import com.ddubok.common.auth.handler.CustomLogoutSuccessHandler;
import com.ddubok.common.auth.jwt.JwtAuthenticationFilter;
import com.ddubok.common.auth.jwt.JwtTokenUtil;
import com.ddubok.common.auth.oauth.CustomAuthorizationRequestRepository;
import com.ddubok.common.auth.oauth.CustomOAuth2FailureHandler;
import com.ddubok.common.auth.oauth.CustomOAuth2SuccessHandler;
import com.ddubok.common.auth.oauth.CustomOAuth2UserService;
import com.ddubok.common.auth.oauth.CustomOidcUserService;
import com.ddubok.common.auth.registration.SocialClientRegistrationConfig;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security 보안 설정 클래스.
 * JWT 인증과 OAuth2 소셜 로그인을 구성한다.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final SocialClientRegistrationConfig socialClientRegistrationConfig;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            Arrays.asList("https://ddubok.com", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie", "Error", "Error-Description"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
        ClientRegistrationRepository clientRegistrationRepository) {

        DefaultOAuth2AuthorizationRequestResolver resolver =
            new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                "/api/oauth2/authorization"
            );

        resolver.setAuthorizationRequestCustomizer(
            authorizationRequestBuilder -> {
                try {
                    OAuth2AuthorizationRequest.Builder builder =
                        (OAuth2AuthorizationRequest.Builder) authorizationRequestBuilder;

                    Map<String, Object> attributes = builder.build().getAttributes();
                    Object registrationIdObj = attributes.get(OAuth2ParameterNames.REGISTRATION_ID);

                    if (registrationIdObj != null && "x".equals(registrationIdObj.toString())) {
                        String codeVerifier = "challenge";
                        Map<String, Object> additionalParams = Map.of(
                            "code_challenge", codeVerifier,
                            "code_challenge_method", "plain",
                            "code_verifier", codeVerifier
                        );
                        builder.additionalParameters(params -> params.putAll(additionalParams));
                    }
                } catch (Exception e) {
                    log.error("Error customizing authorization request", e);
                }
            });

        return resolver;
    }

    /**
     * SecurityFilterChain을 구성한다.
     *
     * 주요 설정:
     * 1. CSRF, 폼 로그인, HTTP Basic 인증 비활성화
     * 2. Frame Options 설정 (same-origin 허용)
     * 3. JWT 인증 필터 추가
     * 4. OAuth2 로그인 설정
     *    - 소셜 로그인 제공자 설정
     *    - 인증 엔드포인트 설정
     *    - 사용자 서비스 설정
     * 5. URL 기반 접근 제어
     * 6. 로그아웃 제어
     * 7. 세션 관리 설정 (STATELESS)
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 보안 설정 관련 예외 발생 시
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        http
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            );

        http
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil),
                UsernamePasswordAuthenticationFilter.class);

        http
            .oauth2Login((oauth2) -> oauth2
                .clientRegistrationRepository(
                    socialClientRegistrationConfig.clientRegistrationRepository())
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService))
                .authorizationEndpoint(endPoint -> endPoint
                    .authorizationRequestRepository(customAuthorizationRequestRepository)
                    .authorizationRequestResolver(customAuthorizationRequestResolver(
                        socialClientRegistrationConfig.clientRegistrationRepository()))
                    .baseUri("/api/oauth2/authorization"))
                .redirectionEndpoint(endPoint -> endPoint.baseUri("/api/login/oauth2/code/*"))
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
            );

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/v1/auth/check-refresh-token", "/api/v1/auth/reissue").permitAll()
                .anyRequest().authenticated()
            );

        http
            .logout(logout ->
                logout
                    .logoutUrl("/api/v1/auth/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .clearAuthentication(true));

        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
