package com.ddubok.common.auth.oauth;

import com.ddubok.common.auth.dto.MemberAuthDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

/**
 * OidcUser 인터페이스의 커스텀 구현체. MemberAuthDto를 통해 사용자 정보를 관리한다.
 */
public class CustomOidcUser extends DefaultOidcUser implements CustomUser {

    private final MemberAuthDto memberAuthDto;

    /**
     * CustomOidcUser 생성자. 사용자의 권한, ID 토큰, 사용자 정보를 설정한다.
     *
     * @param memberAuthDto 회원 인증 정보 DTO
     * @param idToken       OpenID Connect ID 토큰
     * @param userInfo      OpenID Connect 사용자 정보
     */
    public CustomOidcUser(MemberAuthDto memberAuthDto, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(Collections.singletonList(new SimpleGrantedAuthority(memberAuthDto.getRole().name())),
            idToken,
            userInfo);
        this.memberAuthDto = memberAuthDto;
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
}
