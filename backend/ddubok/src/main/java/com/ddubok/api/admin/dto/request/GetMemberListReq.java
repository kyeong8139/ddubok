package com.ddubok.api.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자에 대한 검색 및 필터링요소
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMemberListReq {

    private String searchName;
    private String state;
    private int page = 0;
    private int size = 50;
}
