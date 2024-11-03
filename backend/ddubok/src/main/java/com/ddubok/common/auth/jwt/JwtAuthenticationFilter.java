package com.ddubok.common.auth.jwt;

import com.ddubok.api.member.entity.Role;
import com.ddubok.common.auth.dto.MemberAuthDto;
import com.ddubok.common.auth.oauth.CustomOAuth2User;
import com.ddubok.common.template.response.ResponseCode;
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

/**
 * JWT 인증을 처리하는 필터 클래스. 모든 HTTP 요청에 대해 JWT 토큰을 검증하고 인증 정보를 설정한다. '/api/auth'와 '/oauth' 경로는 필터 검사에서
 * 제외
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    /**
     * JWT 토큰을 검증하고 인증 정보를 설정하는 필터 메서드
     *
     * @param request     HTTP 요청 객체
     * @param response    HTTP 응답 객체
     * @param filterChain 다음 필터로 요청을 전달할 필터 체인
     * @throws ServletException 서블릿 처리 중 발생할 수 있는 예외
     * @throws IOException      입출력 처리 중 발생할 수 있는 예외
     */
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
            String message = "Invalid Access Token1";
            sendErrorResponse(response, message,
                Integer.parseInt(ResponseCode.INVALID_ACCESS_TOKEN.getCode()));
            return;
        }

        String category = jwtTokenUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            String message = "Invalid Access Token2";
            sendErrorResponse(response, message,
                Integer.parseInt(ResponseCode.INVALID_ACCESS_TOKEN.getCode()));
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

    /**
     * 에러 응답을 클라이언트에게 전송하는 헬퍼 메서드 Filter 단에서 에러를 던질 경우 Handler나 Advicer가 작동되지 않아 작성
     *
     * @param response HTTP 응답 객체
     * @param message  에러 메시지
     * @param status   HTTP 상태 코드
     * @throws IOException 입출력 처리 중 발생할 수 있는 예외
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int status)
        throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }
}
