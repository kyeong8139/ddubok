package com.ddubok.api.member.service;

import com.ddubok.api.member.dto.request.UpdateMemberReq;
import com.ddubok.api.member.dto.response.MemberDetailRes;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDetailRes getMemberDetail(Long memberId) {
        Member member = findAndVerifyMember(memberId);
        return MemberDetailRes.builder().nickname(member.getNickname())
            .notificationConsent(member.getNotificationConsent()).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMember(Long memberId, UpdateMemberReq updateMemberDto) {
        Member member = findAndVerifyMember(memberId);
        member.updateNickname(updateMemberDto.getNickname());
    }

    /**
     * 멤버 ID로 멤버를 찾고 검증한다.
     *
     * @param memberId 멤버 ID
     * @return 해당 ID의 멤버
     */
    private Member findAndVerifyMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException());
    }
}
