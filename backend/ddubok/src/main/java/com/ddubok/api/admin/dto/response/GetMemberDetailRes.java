package com.ddubok.api.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMemberDetailRes {

    private Long memberId;
    private String nickname;
    private String role;
    private String state;
}
