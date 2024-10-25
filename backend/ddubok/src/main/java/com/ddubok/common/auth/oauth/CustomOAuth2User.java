package com.ddubok.common.auth.oauth;

import com.ddubok.common.auth.dto.MemberAuthDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * OAuth2User 인터페이스의 커스텀 구현체. MemberAuthDto를 통해 사용자 정보를 관리한다.
 */
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User, CustomUser {

    private final MemberAuthDto memberAuthDto;

    /**
     * OAuth2 사용자의 속성을 반환한다. 현재 구현에서는 빈 Map을 반환한다. 사용하지는 않지만 필수 구현 메서드라 구현함.
     *
     * @return 빈 속성 Map
     */
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    /**
     * 사용자의 권한 목록을 반환한다.
     *
     * @return 사용자 권한 컬렉션
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberAuthDto.getRole().name();
            }
        });

        return collection;
    }

    /**
     * 사용자 이름(닉네님)을 반환한다.
     *
     * @return 사용자 닉네임
     */
    @Override
    public String getName() {
        return memberAuthDto.getNickname();
    }

    /**
     * 사용자 역할을 반환한다.
     *
     * @return 사용자 역할 문자열
     */
    @Override
    public String getRole() {
        return memberAuthDto.getRole().name();
    }

    /**
     * 사용자가 로그인한 소셜 서비스의 accessToken을 반환한다.
     *
     * @return 사용자가 로그인한 소셜 서비스의 accessToken
     */
    @Override
    public String getSocialAccessToken() {
        return memberAuthDto.getSocialAccessToken();
    }

    /**
     * 멤버 ID를 반환한다.
     *
     * @return 멤버 ID
     */
    @Override
    public Long getId() {
        return memberAuthDto.getMemberId();
    }
}