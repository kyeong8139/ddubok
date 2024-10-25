package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.CreateSeasonReqDto;
import com.ddubok.api.admin.dto.response.CreateSeasonRes;
import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.exception.InvalidDateOrderException;
import com.ddubok.api.admin.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    final private SeasonRepository seasonRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateSeasonRes createSeason(CreateSeasonReqDto createSeasonReqDto) {
        validateSeasonDates(createSeasonReqDto);
        Season season = seasonRepository.save(Season.builder()
            .name(createSeasonReqDto.getSeasonName())
            .description(createSeasonReqDto.getSeasonDescription())
            .path(createSeasonReqDto.getPath())
            .startedAt(createSeasonReqDto.getStartedAt())
            .endedAt(createSeasonReqDto.getEndedAt())
            .openedAt(createSeasonReqDto.getOpenedAt())
            .build()
        );
        return CreateSeasonRes.builder()
            .id(season.getId())
            .build();
    }

    /**
     * 시작일, 종료일, 오픈일 검증
     * @param createSeasonReqDto 요청 데이터
     */
    private void validateSeasonDates(CreateSeasonReqDto createSeasonReqDto) {
        if (createSeasonReqDto.getStartedAt().isAfter(createSeasonReqDto.getEndedAt())) {
            throw new InvalidDateOrderException("시작일은 종료일보다 앞서야 합니다.");
        }

        if (createSeasonReqDto.getOpenedAt().isAfter(createSeasonReqDto.getEndedAt())) {
            throw new InvalidDateOrderException("오픈일은 종료일보다 앞서야 합니다.");
        }


    }
}
