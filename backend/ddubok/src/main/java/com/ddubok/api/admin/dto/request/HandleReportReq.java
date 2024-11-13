package com.ddubok.api.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 신고처리에 대한 변경값
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HandleReportReq {

    private String state;
}
