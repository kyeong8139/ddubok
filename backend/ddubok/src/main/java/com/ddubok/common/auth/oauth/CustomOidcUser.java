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

public class CustomOidcUser extends DefaultOidcUser implements CustomUser {

    private final MemberAuthDto memberAuthDto;

    public CustomOidcUser(MemberAuthDto memberAuthDto, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(Collections.singletonList(new SimpleGrantedAuthority(memberAuthDto.getRole().name())),
            idToken,
            userInfo);
        this.memberAuthDto = memberAuthDto;
    }

    @Override
    public Long getId() {
        return memberAuthDto.getMemberId();
    }

    @Override
    public String getRole() {
        return memberAuthDto.getRole().name();
    }

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
