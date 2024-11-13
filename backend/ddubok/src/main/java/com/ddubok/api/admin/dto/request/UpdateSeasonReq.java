package com.ddubok.api.admin.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeasonReq {

    private String name;
    private String seasonDescription;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime openedAt;
}
