package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCardReqDto {

    private String content;
    private String writerName;
    private Long seasonId;
    private String path;
    private Long memberId;
}
