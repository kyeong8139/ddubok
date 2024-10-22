package com.ddubok.common.auth.service;

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

    @Override
    @Transactional
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        /*
            todo : CustomException 구현
         */
        if (refresh == null) {
            throw new RuntimeException("Refresh cookie not found");
        }

        Long memberId = jwtTokenUtil.getMemberId(refresh);
        if (jwtTokenUtil.isExpired(refresh)) {
            redisTemplate.delete("RT:" + memberId);
            throw new RuntimeException("Refresh expired");
        }

        // DB에 저장되어 있는지 확인
        String findRefresh = redisTemplate.opsForValue().get("RT:" + memberId);
        if (findRefresh == null) {
            throw new RuntimeException("Refresh token not found");
        }

        if (!findRefresh.equals(refresh)) {
            throw new RuntimeException("Refresh Error");
        }

        String category = jwtTokenUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new RuntimeException("Refresh token error");
        }

        String role = jwtTokenUtil.getRole(refresh);
        String newAccess = jwtTokenUtil.createToken("access", memberId, role, expiration);
        response.setHeader("Authorization", "Bearer " + newAccess);

    }
}
