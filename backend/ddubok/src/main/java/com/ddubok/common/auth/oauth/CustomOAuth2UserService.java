package com.ddubok.common.auth.oauth;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.Role;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.common.auth.dto.MemberAuthDto;
import com.ddubok.common.auth.dto.OAuth2Response;
import com.ddubok.common.auth.service.NicknameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2ResponseFactory oAuth2ResponseFactory;
    private final MemberRepository memberRepository;
    private final NicknameService nicknameService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = oAuth2ResponseFactory.getOAuth2UserInfo(registrationId,
            oauth2User.getAttributes());
        MemberAuthDto memberAuthDto = processOAuth2User(oAuth2Response);

        return new CustomOAuth2User(memberAuthDto);
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
