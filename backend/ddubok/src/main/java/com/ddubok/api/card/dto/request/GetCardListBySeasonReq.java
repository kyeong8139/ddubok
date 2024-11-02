package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCardListBySeasonReq {

    private Long seasonId;
    private Long memberId;
    private Integer size;
    private Long lastCardId;
}
