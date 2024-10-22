package com.ddubok.common.auth.oauth;

import com.ddubok.common.auth.dto.MemberAuthDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User, CustomUser {

    private final MemberAuthDto memberAuthDto;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberAuthDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return memberAuthDto.getNickname();
    }

    @Override
    public String getRole() {
        return memberAuthDto.getRole();
    }

    @Override
    public Long getId() {
        return memberAuthDto.getMemberId();
    }
}