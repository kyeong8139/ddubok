package com.ddubok.common.auth.dto;

import com.ddubok.api.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthDto {

    private Long memberId;
    private Role role;
    private String nickname;
    private String socialAccessToken;

}
