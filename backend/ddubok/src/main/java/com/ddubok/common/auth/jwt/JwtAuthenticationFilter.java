package com.ddubok.common.auth.jwt;

import com.ddubok.common.auth.dto.MemberAuthDto;
import com.ddubok.common.auth.oauth.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/auth") || requestUri.startsWith("/oauth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = jwtTokenUtil.extractToken(accessToken);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        if (jwtTokenUtil.isExpired(accessToken)) {
            String message = "AccessToken is Expired";
            sendErrorResponse(response, message, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtTokenUtil.getCategory(accessToken);

        // 엑세스 토큰이 아니면 다음 필터로 넘기지 않음
        if (!category.equals("access")) {
            String message = "AccessToken Error";
            sendErrorResponse(response, message, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // userId, role 값을 획득
        Long memberId = jwtTokenUtil.getMemberId(accessToken);
        String role = jwtTokenUtil.getRole(accessToken);

        MemberAuthDto member = MemberAuthDto.builder()
            .memberId(memberId)
            .role(role)
            .build();

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
            customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status)
        throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }
}
