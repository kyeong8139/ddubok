package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.GetMemberListReq;
import com.ddubok.api.admin.dto.response.GetMemberListRes;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.UserState;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.api.report.entity.State;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
        List<Member> memberList = getMembersByConditions(stateString, searchName);
        return memberList.stream()
            .map(member -> GetMemberListRes.builder()
                .memberId(member.getId())
                .nickName(member.getNickname())
                .state(member.getState().toUserStateName())
                .build())
            .collect(Collectors.toList());
    }

    private List<Member> getMembersByConditions(String stateString, String searchName){
        List<Member> memberList = new ArrayList<>();
        if(stateString == null) {
            if(searchName == null) {
                return memberList = memberRepository.findAll();
            }
            return memberList = memberRepository.findByNicknameContaining(searchName);
        }
        UserState userState = UserState.fromUserStateName(stateString);
        if(searchName == null) {
            return memberRepository.findByState(userState);
        }
        return memberRepository.findByStateAndNickname(userState, searchName);
    }
}
