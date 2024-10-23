package com.ddubok.api.member.service;

import com.ddubok.api.member.dto.response.MemberDetailRes;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDetailRes getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        return MemberDetailRes.builder().nickname(member.getNickname()).build();
    }
}
