package com.ddubok.api.card.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardPreviewRes {

    private String nickname;
    private List<String> cardUrl;

}
