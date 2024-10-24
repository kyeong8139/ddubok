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

    /**
     * OAuth2 인증 완료 후 사용자 정보를 로드하고 처리한다. 기존 사용자가 아닌 경우 새로운 회원으로 등록한다.
     * <p>
     * 처리 과정: 1. OidcUserService 통해 OAuth2 사용자 정보 로드 2. OAuth2Response 객체 생성 3. 회원 정보 조회 또는 생성 4.
     * CustomOidcUser 객체 생성 및 반환
     *
     * @param userRequest 사용자 요청 정보
     * @return CustomOidcUser 인스턴스
     * @throws OAuth2AuthenticationException OAuth2 인증 처리 중 발생한 예외
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = oAuth2ResponseFactory.getOAuth2UserInfo(registrationId,
            oidcUser.getAttributes());

        String socialAccessToken = userRequest.getAccessToken().getTokenValue();
        MemberAuthDto memberAuthDto = processOAuth2User(oAuth2Response, socialAccessToken);

        return new CustomOidcUser(memberAuthDto, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    /**
     * OAuth2 인증 정보를 기반으로 회원 정보를 처리한다. 기존 회원이 아닌 경우 새로운 회원을 생성한다. 회원 생성 시 설정되는 정보: - 소셜 ID (제공자별 고유
     * 식별자) - 소셜 제공자 (GOOGLE, META) - 닉네임 (자동 생성) - 역할 (ROLE_USER)
     *
     * @param oAuth2Response OAuth2 인증 응답 정보
     * @return 회원 인증 정보 DTO
     */
    private MemberAuthDto processOAuth2User(OAuth2Response oAuth2Response,
        String socialAccessToken) {
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
            .memberId(member.getId())
            .role(member.getRole())
            .nickname(member.getNickname())
            .socialAccessToken(socialAccessToken)
            .build();
    }
}