package com.ddubok.api.card.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCardListRes {

    @JsonProperty(value = "hasNext")
    private boolean hasNext;
    List<GetCardDetailRes> cards;
    private Long total;
}
