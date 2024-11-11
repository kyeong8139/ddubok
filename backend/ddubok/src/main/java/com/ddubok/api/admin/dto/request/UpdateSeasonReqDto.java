package com.ddubok.api.admin.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeasonReqDto {

    private String seasonName;
    private String seasonDescription;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime openedAt;
    private List<String> path;
}
