package com.ddubok.common.config;

import com.ddubok.common.auth.jwt.JwtAuthenticationFilter;
import com.ddubok.common.auth.jwt.JwtTokenUtil;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 보안 설정 클래스.
 * JWT 인증과 OAuth2 소셜 로그인을 구성한다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final SocialClientRegistrationConfig socialClientRegistrationConfig;

    /**
     * AuthenticationManager 빈을 구성한다.
     *
     * @param configuration 인증 설정 객체
     * @return AuthenticationManager 인스턴스
     * @throws Exception 인증 관련 예외 발생 시
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
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
     * 6. 세션 관리 설정 (STATELESS)
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 보안 설정 관련 예외 발생 시
     */
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
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil),
                UsernamePasswordAuthenticationFilter.class);

        http
            .oauth2Login((oauth2) -> oauth2
                .clientRegistrationRepository(
                    socialClientRegistrationConfig.clientRegistrationRepository())
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService))
                .authorizationEndpoint(endPoint -> endPoint.baseUri("/api/oauth2/authorization"))
                .redirectionEndpoint(endPoint -> endPoint.baseUri("/api/login/oauth2/code/*"))
                .successHandler(customOAuth2SuccessHandler));

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/logout", "/api/v1/auth/**").permitAll()
                .anyRequest().permitAll()
            );

        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
