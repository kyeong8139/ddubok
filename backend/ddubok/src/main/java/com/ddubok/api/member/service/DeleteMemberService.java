package com.ddubok.api.member.service;

public interface DeleteMemberService {

    /**
     * 멤버를 탈퇴시킨다.
     *
     * @param memberId 멤버 ID
     */
    void deleteMember(Long memberId);

}
