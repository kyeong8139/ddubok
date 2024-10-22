package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCardReqDto {

    private String content;
    private String writerName;
    private Boolean isCustom;
    private Long seasonId;
    private String path;
}
