package com.ddubok.common.auth.oauth;

import com.ddubok.common.auth.exception.UnsupportedOAuth2UserTypeException;
import com.ddubok.common.auth.jwt.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2 인증 성공 시 후처리를 담당하는 핸들러. refresh 토큰을 생성하여 쿠키에 저장하고 Redis에 캐싱
 */
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.jwt.refresh-token.expiration}")
    private Long expiration;
    @Value("${spring.jwt.redirect-url}")
    private String redirectUrl;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final String REDIS_REFRESH_TOKEN_PREFIX = "RT:";
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh";

    /**
     * OAuth2 인증 성공 시 실행되는 메서드 CustomUser 정보를 추출하여 refresh 토큰을 생성하고, 생성된 토큰을 쿠키에 저장하고 redis에 캐싱한다.
     *
     * @param request        HTTP 요청 객체
     * @param response       HTTP 응답 객체
     * @param authentication 인증 정보
     * @throws IOException              입출력 처리 중 발생할 수 있는 예외
     * @throws ServletException         서블릿 처리 중 발생할 수 있는 예외
     * @throws IllegalArgumentException 지원하지 않는 사용자 타입인 경우
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomUser customUser;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOidcUser) {
            customUser = (CustomOidcUser) principal;
        } else if (principal instanceof CustomOAuth2User) {
            customUser = (CustomOAuth2User) principal;
        } else {
            throw new UnsupportedOAuth2UserTypeException("해당 유저 타입(" + principal.getClass()
                + ")은 지원하지 않습니다.");
        }

        long userId = customUser.getId();
        String role = customUser.getRole();
        String socialAccessToken = customUser.getSocialAccessToken();

        if (role.equals("ROLE_PRISONER")) {
            response.sendRedirect(redirectUrl + "/jail");
        } else {
            String refreshToken = jwtTokenUtil.createToken(REFRESH_TOKEN_COOKIE_NAME, userId, role,
                socialAccessToken, expiration);
            Cookie refreshCookie = createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
            saveRefreshTokenToRedis(userId, refreshToken);

            response.addCookie(refreshCookie);
            response.sendRedirect(redirectUrl);
        }
    }

    /**
     * refresh 토큰을 redis에 저장한다.
     *
     * @param memberId     멤버 ID
     * @param refreshToken refresh 토큰
     */
    private void saveRefreshTokenToRedis(long memberId, String refreshToken) {
        String key = REDIS_REFRESH_TOKEN_PREFIX + memberId;

        redisTemplate.delete(key);

        Duration ttl = Duration.ofDays(7);
        redisTemplate.opsForValue().set(key, refreshToken, ttl);
    }

    /**
     * 쿠키를 생성한다. 보안을 위해 secure 플래그를 설정한다.
     *
     * @param key   쿠키 키
     * @param value 쿠키 값
     * @return 생성된 쿠키 객체
     */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge((int) (expiration / 1000));
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
