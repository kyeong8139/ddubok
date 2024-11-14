package com.ddubok.api.admin.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetReportDetailRes {

    private Long id;
    private String title;
    private String state;
    private String content;
    private String reportType;
    private Long reportMemberId;
    private String reportMemberNickname;
    private Long cardId;
    private String cardContent;
    private String cardPath;
}
