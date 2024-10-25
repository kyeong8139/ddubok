package com.ddubok.common.auth.handler;

import com.ddubok.common.auth.exception.InvalidRefreshTokenException;
import com.ddubok.common.auth.jwt.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 로그아웃 성공 시 처리를 담당하는 핸들러 클래스. refresh 토큰을 쿠키에서 제거하고 Redis에서도 삭제한다.
 */
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh";
    private static final String REDIS_REFRESH_TOKEN_PREFIX = "RT:";

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 로그아웃 요청 시 실행되는 메서드 refresh 토큰을 쿠키에서 찾아 해당 사용자의 Redis 저장소에서 토큰을 제거하고, 쿠키를 만료시킨다.
     *
     * @param request        HTTP 요청 객체
     * @param response       HTTP 응답 객체
     * @param authentication 인증 정보 객체
     * @throws IOException      입출력 처리 중 발생할 수 있는 예외
     * @throws ServletException 서블릿 처리 중 발생할 수 있는 예외
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        try {
            Cookie refreshCookie = findRefreshTokenCookie(request);
            if (refreshCookie == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Long userId = jwtTokenUtil.getMemberId(refreshCookie.getValue());
            processLogout(userId, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * HTTP 요청에서 refresh 토큰 쿠키를 찾아 반환한다.
     *
     * @param request HTTP 요청 객체
     * @return refresh 토큰 쿠키, 존재하지 않을 경우 null
     */
    private Cookie findRefreshTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
            .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .orElse(null);
    }

    /**
     * 실제 로그아웃 처리를 수행한다. Redis에서 사용자의 refresh 토큰을 삭제하고, 클라이언트의 쿠키를 만료시킨다.
     *
     * @param userId   로그아웃할 사용자의 ID
     * @param response HTTP 응답 객체
     */
    private void processLogout(Long userId, HttpServletResponse response) {
        String redisKey = REDIS_REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.delete(redisKey);

        /*
            todo : 인프라 도메인 정책에 따라 쿠키 정책 수정 필요
         */
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
//        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
