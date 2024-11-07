package com.ddubok.common.auth.util;


import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.common.auth.oauth.CustomOAuth2User;
import com.ddubok.common.auth.oauth.CustomOidcUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final MemberRepository memberRepository;

    public Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = -1L;
        if (authentication.getPrincipal() instanceof CustomOAuth2User user) {
            userId = user.getId();
        } else {
            CustomOidcUser user = (CustomOidcUser) authentication.getPrincipal();
            userId = user.getId();
        }

        if (!memberRepository.existsById(userId)) {
            throw new MemberNotFoundException(userId + "번 유저가 존재하지 않습니다.");
        }

        return userId;
    }
}
