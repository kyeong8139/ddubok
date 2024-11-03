package com.ddubok.common.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰의 생성, 파싱, 검증을 처리하는 유틸리티 클래스. HS256 알고리즘을 사용하여 토큰을 서명한다.
 */
@Slf4j
@Component
public class JwtTokenUtil {

    private SecretKey secretKey;

    /**
     * 시크릿 키를 초기화하는 생성자.
     *
     * @param secret application.yml 설정된 JWT 시크릿 키
     */
    public JwtTokenUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토큰에서 멤버 ID를 추출한다.
     *
     * @param token JWT 토큰
     * @return 회원 ID
     * @throws JwtException 토큰이 유효하지 않은 경우
     */
    public Long getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("memberId", Long.class);
    }

    /**
     * 토큰에서 카테고리를 추출한다.
     *
     * @param token JWT 토큰
     * @return 카테고리 (예: "refresh", "access")
     * @throws JwtException 토큰이 유효하지 않은 경우
     */
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("category", String.class);
    }

    /**
     * 토큰에서 카테고리를 추출한다.
     *
     * @param token JWT 토큰
     * @return 역할 (예: "ROLE_USER", "ROLE_ADMIN")
     * @throws JwtException 토큰이 유효하지 않은 경우
     */
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("role", String.class);
    }

    /**
     * 토큰에서 사용자의 해당 소셜 accessToken을 추출한다.
     *
     * @param token JWT 토큰
     * @return 해당 소셜 accessToken
     * @throws JwtException 토큰이 유효하지 않은 경우
     */
    public String getSocialAccessToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("socialAccessToken", String.class);
    }

    /**
     * 토큰 만료 여부를 확인한다.
     *
     * @param token JWT 토큰
     * @return 만료되었으면 true, 유효하면 false
     */
    public Boolean isExpired(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            log.error("JWT 검증 중 오류 발생: {}", e.getMessage(), e);
            log.error("JWT 검증 중 오류 발생. Token: {}, Error: {}",
                token.substring(0, Math.min(10, token.length())),
                e.getMessage(),
                e);
            return true;
        }
    }

    /**
     * Authorization 헤더에서 Bearer 토큰을 추출한다. "Bearer " 접두사를 제거하고 실제 토큰만 반환한다.
     *
     * @param token Authorization 헤더 값
     * @return Bearer 접두사가 제거된 토큰, 유효하지 않은 형식이면 null
     */
    public String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return null;
    }

    /**
     * 새로운 JWT 토큰을 생성한다.
     *
     * @param category  토큰 카테고리 (예 : "access", "refresh")
     * @param memberId  멤버 ID
     * @param role      멤버 역할
     * @param expiredMs 토큰 만료 시간(밀리초)
     * @return 생성된 JWT 토큰
     */
    public String createToken(String category, long memberId, String role, String socialAccessToken,
        Long expiredMs) {
        return Jwts.builder()
            .claim("category", category)
            .claim("memberId", memberId)
            .claim("role", role)
            .claim("socialAccessToken", socialAccessToken)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

    public String createToken(String category, long memberId, String role, Long expiredMs) {
        return Jwts.builder()
            .claim("category", category)
            .claim("memberId", memberId)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

}
