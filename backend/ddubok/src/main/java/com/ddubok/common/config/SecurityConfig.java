package com.ddubok.common.config;

import com.ddubok.common.auth.handler.CustomLogoutSuccessHandler;
import com.ddubok.common.auth.jwt.JwtAuthenticationFilter;
import com.ddubok.common.auth.jwt.JwtTokenUtil;
import com.ddubok.common.auth.oauth.CustomOAuth2FailureHandler;
import com.ddubok.common.auth.oauth.CustomOAuth2SuccessHandler;
import com.ddubok.common.auth.oauth.CustomOAuth2UserService;
import com.ddubok.common.auth.oauth.CustomOidcUserService;
import com.ddubok.common.auth.registration.SocialClientRegistrationConfig;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
 * Spring Security 보안 설정 클래스. JWT 인증과 OAuth2 소셜 로그인을 구성한다.
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            Arrays.asList("https://ddubok.com", "http://localhost:3000",
                "https://ddubok-test.kro.kr"));
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(
            Arrays.asList("Authorization", "Set-Cookie", "Error", "Error-Description"));

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
            builder -> {
                Map<String, Object> attributes = builder.build().getAttributes();
                String registrationId = (String) attributes.get(
                    OAuth2ParameterNames.REGISTRATION_ID);

                if ("x".equals(registrationId)) {
                    String codeVerifier = generateCodeVerifier();
                    String codeChallenge = generateCodeChallenge(codeVerifier);

                    builder.additionalParameters(params -> {
                        params.put("code_challenge", codeChallenge);
                        params.put("code_challenge_method", "S256");
                    });

                    builder.attributes(attrs ->
                        attrs.put("code_verifier", codeVerifier)
                    );
                }
            });

        return resolver;
    }

    private String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[96];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(codeVerifier);
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate code challenge", e);
        }
    }

    /**
     * SecurityFilterChain을 구성한다.
     * <p>
     * 주요 설정: 1. CSRF, 폼 로그인, HTTP Basic 인증 비활성화 2. Frame Options 설정 (same-origin 허용) 3. JWT 인증 필터
     * 추가 4. OAuth2 로그인 설정 - 소셜 로그인 제공자 설정 - 인증 엔드포인트 설정 - 사용자 서비스 설정 5. URL 기반 접근 제어 6. 로그아웃 제어 7.
     * 세션 관리 설정 (STATELESS)
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
                .requestMatchers("/api/v1/members").hasRole("USER")
                .requestMatchers("/api/v1/admins/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/attendances").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/cards").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v2/cards").permitAll()
                .requestMatchers("/api/v1/cards/receive/**").permitAll()
                .requestMatchers("/api/v1/cards/**").hasRole("USER")
                .requestMatchers("/api/v1/reports").hasRole("USER")
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
