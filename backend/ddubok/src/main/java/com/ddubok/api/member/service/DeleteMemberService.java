package com.ddubok.api.member.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface DeleteMemberService {

    /**
     * 멤버를 탈퇴시킨다.
     *
     * @param memberId 멤버 ID
     */
    void deleteMember(Long memberId, Authentication authentication, HttpServletResponse response);

}
