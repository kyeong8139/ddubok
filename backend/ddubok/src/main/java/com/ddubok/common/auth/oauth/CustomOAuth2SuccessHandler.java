package com.ddubok.common.auth.oauth;

import com.ddubok.api.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomUser customUser;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOidcUser) {  // CustomOidcUser 먼저 체크
            customUser = (CustomOidcUser) principal;
        } else if (principal instanceof CustomOAuth2User) {
            customUser = (CustomOAuth2User) principal;
        } else {
            throw new IllegalArgumentException("Unsupported user type: " + principal.getClass());
        }

        long userId = customUser.getId();
        String role = customUser.getRole();

        response.sendRedirect("/loading");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        response.sendRedirect("");
    }
}
