package com.ddubok.api.card.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetAllCardListReq {

    private Long memberId;
    @Builder.Default
    private int size = 6;
    @Builder.Default
    private int page = 0;
}
