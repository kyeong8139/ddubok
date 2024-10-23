package com.ddubok.common.auth.oauth;

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

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.jwt.refresh-token.expiration}")
    private Long expiration;
    @Value("${spring.jwt.redirect-url}")
    private String redirectUrl;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

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
            throw new IllegalArgumentException("Unsupported user type: " + principal.getClass());
        }

        long userId = customUser.getId();
        String role = customUser.getRole();

        String refreshToken = jwtTokenUtil.createToken("refresh", userId, role,
            expiration);
        Cookie refreshCookie = createCookie("refresh", refreshToken);
        saveRefreshTokenToRedis(userId, refreshToken);

        response.addCookie(refreshCookie);
        response.sendRedirect(redirectUrl);
    }

    private void saveRefreshTokenToRedis(long userId, String refreshToken) {
        String key = "RT:" + userId;

        redisTemplate.delete(key);

        Duration ttl = Duration.ofDays(7);
        redisTemplate.opsForValue().set(key, refreshToken, ttl);
    }

    private Cookie createCookie(String key, String value) {
        /*
            todo : 도메인 설정에 따라 cookie 정책 변경
         */
        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge((int) (expiration / 1000));
        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);

        return cookie;
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        response.sendRedirect("");
    }
}
