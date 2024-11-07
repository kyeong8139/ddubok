package com.ddubok.common.auth.service;

import com.ddubok.common.auth.exception.InvalidRefreshTokenException;
import com.ddubok.common.auth.exception.IsNotExistedResfreshTokenException;
import com.ddubok.common.auth.jwt.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${spring.jwt.expiration}")
    private Long expiration;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final String REDIS_REFRESH_TOKEN_PREFIX = "RT:";
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh";
    private final String ACCESS_TOKEN_COOKIE_NAME = "access";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new InvalidRefreshTokenException("Refresh cookie not found");
        }

        Long memberId = jwtTokenUtil.getMemberId(refresh);
        if (jwtTokenUtil.isExpired(refresh)) {
            redisTemplate.delete(REDIS_REFRESH_TOKEN_PREFIX + memberId);
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String findRefresh = redisTemplate.opsForValue().get(REDIS_REFRESH_TOKEN_PREFIX + memberId);
        if (findRefresh == null) {
            redisTemplate.delete(REDIS_REFRESH_TOKEN_PREFIX + memberId);
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        if (!findRefresh.equals(refresh)) {
            redisTemplate.delete(REDIS_REFRESH_TOKEN_PREFIX + memberId);
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String category = jwtTokenUtil.getCategory(refresh);
        if (!category.equals(REFRESH_TOKEN_COOKIE_NAME)) {
            redisTemplate.delete(REDIS_REFRESH_TOKEN_PREFIX + memberId);
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String role = jwtTokenUtil.getRole(refresh);
        String newAccess = jwtTokenUtil.createToken(ACCESS_TOKEN_COOKIE_NAME, memberId, role, expiration);
        response.setHeader("Authorization", newAccess);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRefreshToken(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if(refresh == null || refresh.trim().isEmpty()) {
            throw new IsNotExistedResfreshTokenException("Refresh token not found or empty");
        }
    }
}
