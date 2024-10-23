package com.ddubok.api.member.controller;

import com.ddubok.api.member.service.MemberService;
import com.ddubok.common.auth.util.AuthUtil;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthUtil authUtil;

    @GetMapping
    public BaseResponse<?> getMemberDetail() {
        Long memberId = authUtil.getMemberId();
        return BaseResponse.ofSuccess(memberService.getMemberDetail(memberId));
    }

}
