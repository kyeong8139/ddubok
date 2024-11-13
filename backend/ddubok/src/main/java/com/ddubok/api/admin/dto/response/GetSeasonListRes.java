package com.ddubok.api.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSeasonListRes {

    private Long id;
    private String name;
    @JsonProperty("isActiveSeason")
    private boolean isActiveSeason;
}
