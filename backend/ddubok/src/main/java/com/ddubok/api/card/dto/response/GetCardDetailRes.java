package com.ddubok.api.card.dto.response;

import com.ddubok.api.card.entity.State;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCardDetailRes {

    private Long id;
    private String content;
    private LocalDateTime openedAt;
    private String path;
    private State state;
    private String writerName;
}
