package com.ddubok.api.admin.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveSeasonReq {
    private Long seasonId;
    private String seasonDescription;
    private List<String> path;
}
