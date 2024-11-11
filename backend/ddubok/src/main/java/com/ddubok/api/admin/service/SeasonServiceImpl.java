package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.CreateSeasonReqDto;
import com.ddubok.api.admin.dto.request.UpdateSeasonReqDto;
import com.ddubok.api.admin.dto.response.CreateSeasonRes;
import com.ddubok.api.admin.dto.response.GetReportListRes;
import com.ddubok.api.admin.dto.response.GetSeasonDetailRes;
import com.ddubok.api.admin.dto.response.GetSeasonListRes;
import com.ddubok.api.admin.dto.response.UpdateSeasonRes;
import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.exception.InvalidDateOrderException;
import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import com.ddubok.api.member.exception.MemberNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
        validateSeasonDates(createSeasonReqDto.getStartedAt(), createSeasonReqDto.getEndedAt());
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
     * {@inheritDoc}
     */
    @Override
    public GetSeasonDetailRes getSeasonDetail(Long seasonId) {
        Season season = seasonRepository.findById(seasonId)
            .orElseThrow(() -> new SeasonNotFoundException("시즌 번호가 정확하지 않습니다 : " + seasonId));
        return GetSeasonDetailRes.builder()
            .id(season.getId())
            .name(season.getName())
            .seasonDescription(season.getDescription())
            .startedAt(season.getStartedAt())
            .endedAt(season.getEndedAt())
            .openedAt(season.getOpenedAt())
            .path(season.getPath())
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetSeasonListRes> getSeasonList() {
        List<Season> seasons = seasonRepository.findAll();
        return seasons.stream()
            .sorted(Comparator.comparing(Season::getId).reversed())  // Season 객체의 id 기준으로 역순 정렬
            .map(season -> GetSeasonListRes.builder()
                    .id(season.getId())
                    .name(season.getName())
                    .build())
                .collect(Collectors.toList());
    }

    @Override
    public UpdateSeasonRes updateSeason(Long seasonId, UpdateSeasonReqDto updateSeasonReqDto) {
        validateSeasonDates(updateSeasonReqDto.getStartedAt(), updateSeasonReqDto.getEndedAt());
        Season season = seasonRepository.findById(seasonId)
            .orElseThrow(() -> new SeasonNotFoundException("해당 번호의 시즌을 찾을 수 없습니다." + seasonId));
        Season upDateSeason = seasonRepository.save(Season.builder()
            .id(seasonId)
            .name(updateSeasonReqDto.getSeasonName())
            .description(updateSeasonReqDto.getSeasonDescription())
            .path(updateSeasonReqDto.getPath())
            .startedAt(updateSeasonReqDto.getStartedAt())
            .endedAt(updateSeasonReqDto.getEndedAt())
            .openedAt(updateSeasonReqDto.getOpenedAt())
            .build());
        return UpdateSeasonRes.builder()
            .id(upDateSeason.getId())
            .build();
    }

    /**
     * 시작일, 종료일 검증
     *
     * @param startedAt 시즌 시작일
     * @param endedAt 시즌 종료일
     */
    private void validateSeasonDates(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (startedAt.isAfter(endedAt)) {
            throw new InvalidDateOrderException("시작일은 종료일보다 앞서야 합니다.");
        }
    }
}
