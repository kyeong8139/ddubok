package com.ddubok.api.member.controller;

import com.ddubok.api.member.dto.request.UpdateMemberReq;
import com.ddubok.api.member.service.DeleteMemberService;
import com.ddubok.api.member.service.MemberService;
import com.ddubok.common.auth.util.AuthUtil;
import com.ddubok.common.template.response.BaseResponse;
import com.ddubok.common.template.response.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final DeleteMemberService deleteMemberService;
    private final AuthUtil authUtil;

    @GetMapping
    public BaseResponse<?> getMemberDetail() {
        Long memberId = authUtil.getMemberId();
        return BaseResponse.ofSuccess(memberService.getMemberDetail(memberId));
    }

    @PatchMapping
    public BaseResponse<?> updateMember(@RequestBody UpdateMemberReq reqDto) {
        Long memberId = authUtil.getMemberId();
        memberService.updateMember(memberId, reqDto);
        return BaseResponse.ofSuccess(ResponseCode.MEMBER_UPDATED);
    }

    @DeleteMapping
    public BaseResponse<?> deleteMember(Authentication authentication, HttpServletResponse response) {
        Long memberId = authUtil.getMemberId();
        deleteMemberService.deleteMember(memberId, authentication, response);
        return BaseResponse.ofSuccess(ResponseCode.DELETED);
    }
}
