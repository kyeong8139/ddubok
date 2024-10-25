package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.CreateSeasonReqDto;
import com.ddubok.api.admin.dto.response.CreateSeasonRes;

/**
 * 시즌 관련 처리를 위한 서비스
 */
public interface SeasonService {

    /**
     * 관리자가 시즌을 등록한다
     *
     * @param createSeasonReqDto 등록할 시즌의 정보
     * @return 등록된 시즌의 번호를 반환한다.
     */
    CreateSeasonRes createSeason(CreateSeasonReqDto createSeasonReqDto);
}
