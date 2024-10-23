package com.ddubok.api.member.service;

import com.ddubok.api.member.dto.response.MemberDetailRes;

public interface MemberService {

    /**
     * 멤버 ID로 멤버 정보를 조회한다.
     *
     * @param memberId 멤버 ID
     * @return 해당 멤버 정보
     */
    MemberDetailRes getMemberDetail(Long memberId);

}
