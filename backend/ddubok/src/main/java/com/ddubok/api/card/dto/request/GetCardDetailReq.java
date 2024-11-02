package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCardDetailReq {

    private Long cardId;
    private Long memberId;
}
