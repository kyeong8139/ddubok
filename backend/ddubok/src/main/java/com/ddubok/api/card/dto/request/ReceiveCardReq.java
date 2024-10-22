package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReceiveCardReq {

    private Long cardId;
    private Long memberId;
}
