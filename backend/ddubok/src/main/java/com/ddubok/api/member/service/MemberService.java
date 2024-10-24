package com.ddubok.api.member.service;

import com.ddubok.api.member.dto.request.UpdateMemberReq;
import com.ddubok.api.member.dto.response.MemberDetailRes;

public interface MemberService {

    /**
     * 멤버 ID로 멤버 정보를 조회한다.
     *
     * @param memberId 멤버 ID
     * @return 해당 멤버 정보
     */
    MemberDetailRes getMemberDetail(Long memberId);

    /**
     * 멤버 정보를 수정한다.
     *
     * @param memberId        멤버 ID
     * @param updateMemberDto 변경할 정보
     */
    void updateMember(Long memberId, UpdateMemberReq updateMemberDto);

}
