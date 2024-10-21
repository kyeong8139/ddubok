package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCardReq {

    private Long receiveMemberId;
    private String content;
    private String writerName;
    private Long seasonId;
}
