package com.ddubok.api.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportMemberReq {

    private Long cardId;
    private String title;
    private String reportType;
    private String content;
}
