package com.ddubok.common.auth.jwt;

import com.ddubok.api.member.entity.Role;
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

        String accessToken = request.getHeader("Authorization");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = jwtTokenUtil.extractToken(accessToken);

        if (jwtTokenUtil.isExpired(accessToken)) {
            String message = "AccessToken is Expired";
            sendErrorResponse(response, message, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtTokenUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            String message = "AccessToken Error";
            sendErrorResponse(response, message, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long memberId = jwtTokenUtil.getMemberId(accessToken);
        String role = jwtTokenUtil.getRole(accessToken);
        Role userRole = Role.valueOf(role.toUpperCase());

        MemberAuthDto member = MemberAuthDto.builder()
            .memberId(memberId)
            .role(userRole)
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
