package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetAllCardListReq {

    private Long memberId;
    private Integer size;
    private Long lastCardId;
}
