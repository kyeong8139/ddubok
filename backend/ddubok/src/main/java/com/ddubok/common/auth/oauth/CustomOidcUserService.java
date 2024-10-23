package com.ddubok.common.auth.oauth;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.Role;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.common.auth.dto.MemberAuthDto;
import com.ddubok.common.auth.dto.OAuth2Response;
import com.ddubok.common.auth.service.NicknameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final OAuth2ResponseFactory oAuth2ResponseFactory;
    private final MemberRepository memberRepository;
    private final NicknameService nicknameService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = oAuth2ResponseFactory.getOAuth2UserInfo(registrationId,
            oidcUser.getAttributes());
        MemberAuthDto memberAuthDto = processOAuth2User(oAuth2Response);

        return new CustomOidcUser(memberAuthDto, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    private MemberAuthDto processOAuth2User(OAuth2Response oAuth2Response) {
        String id = oAuth2Response.getProviderId();
        String socialProvider = oAuth2Response.getProvider();

        Member member = memberRepository.findBySocialId(id)
            .orElseGet(() -> {
                Member newMember = Member.builder()
                    .socialId(id)
                    .socialProvider(socialProvider)
                    .nickname(nicknameService.createNickname())
                    .role(Role.ROLE_USER)
                    .build();
                return memberRepository.save(newMember);
            });

        return MemberAuthDto.builder()
            .nickname(member.getNickname())
            .role(member.getRole())
            .memberId(member.getId())
            .build();
    }
}