package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.GetMemberListReq;
import com.ddubok.api.admin.dto.response.GetMemberDetailRes;
import com.ddubok.api.admin.dto.response.GetMemberListRes;
import com.ddubok.api.admin.dto.response.UpdateMemberRoleRes;
import com.ddubok.api.admin.dto.response.UpdateMemberStateRes;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.UserState;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberStatusServiceImpl implements MemberStatusService {

    private final MemberRepository memberRepository;

    /**
     * @inheritDoc
     */
    @Override
    public List<GetMemberListRes> getMemberList(GetMemberListReq getMemberListReq) {
        String stateString = getMemberListReq.getState();
        String searchName = getMemberListReq.getSearchName();

        Pageable pageable = PageRequest.of(getMemberListReq.getPage(), getMemberListReq.getSize(), Sort.by("id").descending());

        Page<Member> memberPage = getMembersByConditions(stateString, searchName, pageable);

        return memberPage.stream()
            .map(member -> GetMemberListRes.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .state(member.getState().toUserStateName())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public GetMemberDetailRes getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("유저 번호가 정확하지 않습니다 : " + memberId));
        return GetMemberDetailRes.builder()
            .memberId(member.getId())
            .nickname(member.getNickname())
            .role(member.getRole().toRoleName())
            .state(member.getState().toUserStateName())
            .build();
    }

    /**
     * @inheritDoc
     */
    @Override
    public UpdateMemberRoleRes updateMemberRole(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new MemberNotFoundException("유저 번호가 정확하지 않습니다 : " + memberId));
        member.updateRole(member.getRole());
        return UpdateMemberRoleRes.builder()
            .id(member.getId())
            .roleName(member.getRole().toRoleName())
            .build();
    }

    /**
     * @inheritDoc
     */
    @Override
    public UpdateMemberStateRes updateMemberState(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new MemberNotFoundException("유저 번호가 정확하지 않습니다 : " + memberId));
        member.updateUserState(member.getState());
        return UpdateMemberStateRes.builder()
            .id(member.getId())
            .state(member.getState().toUserStateName())
            .build();
    }

    private Page<Member> getMembersByConditions(String stateString, String searchName, Pageable pageable) {
        if(stateString == null) {
            if(searchName == null) {
                return memberRepository.findAll(pageable);
            }
            return memberRepository.findByNicknameContaining(searchName,pageable);
        }
        UserState userState = UserState.fromUserStateName(stateString);
        if(searchName == null) {
            return memberRepository.findByState(userState,pageable);
        }
        return memberRepository.findByStateAndNickname(userState, searchName,pageable);
    }
}
