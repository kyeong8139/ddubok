package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.GetMemberListReq;
import com.ddubok.api.admin.dto.response.GetMemberListRes;
import java.util.List;

/**
 * 멤버에 대한 제지를 위한 서비스
 */
public interface MemberStatusService {

    /**
     * 관리자가 사용자 목록을 조회한다
     *
     * @param getMemberListReq 검색할 닉네임 키워드와 상태 정보를 포함하고 있는 객체
     * @return 조건에 맞는 사용자들의 목록을 반환
     */
    List<GetMemberListRes> getMemberList(GetMemberListReq getMemberListReq);
}
