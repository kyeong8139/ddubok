package com.ddubok.api.admin.dto.response;

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
public class MainSeasonRes {
    private Long seasonId;
    private String seasonDescription;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private List<String> path;
}
