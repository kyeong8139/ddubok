package com.ddubok.api.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetReportDetailRes {

    private String title;
    private String type;
    private Long cardId;
    private String content;
}
