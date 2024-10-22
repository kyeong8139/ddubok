package com.ddubok.common.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthDto {

    private Long memberId;
    private String role;
    private String nickname;

}
