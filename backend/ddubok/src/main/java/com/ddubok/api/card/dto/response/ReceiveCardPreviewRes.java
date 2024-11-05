package com.ddubok.api.card.dto.response;

import com.ddubok.api.card.entity.State;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReceiveCardPreviewRes {

    private Long id;
    private String content;
    private String writerName;
    private String path;
    private State state;
    private LocalDateTime openedAt;
}
