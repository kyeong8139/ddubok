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
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException());
        return MemberDetailRes.builder().nickname(member.getNickname()).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMember(Long memberId, UpdateMemberReq updateMemberDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException());

        member.updateNickname(updateMemberDto.getNickname());
    }
}
