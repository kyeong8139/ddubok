package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.CreateSeasonReqDto;
import com.ddubok.api.admin.dto.request.UpdateSeasonReqDto;
import com.ddubok.api.admin.dto.response.MainSeasonRes;
import com.ddubok.api.admin.dto.response.CreateSeasonRes;
import com.ddubok.api.admin.dto.response.DefaultSeasonRes;
import com.ddubok.api.admin.dto.response.GetSeasonDetailRes;
import com.ddubok.api.admin.dto.response.GetSeasonListRes;
import com.ddubok.api.admin.dto.response.UpdateSeasonRes;
import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.exception.InvalidDateOrderException;
import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SeasonRepository seasonRepository;
    private final String SEASON_START_DATE_KEY = "main:next:date";
    private final String ACTIVE_SEASON_KEY = "main:active";
    private final String DEFAULT_SEASON_KEY = "main:default";

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
        updateNextSeasonDate();
        setExpirationForNotification(season);
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
            .sorted(Comparator.comparing(Season::getId).reversed())
            .map(season -> GetSeasonListRes.builder()
                .id(season.getId())
                .name(season.getName())
                .isActiveSeason(season.getStartedAt().isBefore(LocalDateTime.now())
                    && season.getEndedAt().isAfter(LocalDateTime.now()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public UpdateSeasonRes updateSeason(Long seasonId, UpdateSeasonReqDto updateSeasonReqDto) {
        validateSeasonDates(updateSeasonReqDto.getStartedAt(), updateSeasonReqDto.getEndedAt());
        Season season = seasonRepository.findById(seasonId)
            .orElseThrow(() -> new SeasonNotFoundException("해당 번호의 시즌을 찾을 수 없습니다." + seasonId));
        Season updateSeason = seasonRepository.save(Season.builder()
            .id(seasonId)
            .name(updateSeasonReqDto.getSeasonName())
            .description(updateSeasonReqDto.getSeasonDescription())
            .path(updateSeasonReqDto.getPath())
            .startedAt(updateSeasonReqDto.getStartedAt())
            .endedAt(updateSeasonReqDto.getEndedAt())
            .openedAt(updateSeasonReqDto.getOpenedAt())
            .build());
        updateNextSeasonDate();
        setExpirationForNotification(updateSeason);
        return UpdateSeasonRes.builder()
            .id(updateSeason.getId())
            .build();
    }

    @Override
    public DefaultSeasonRes getDefaultSeason() {
        MainSeasonRes mainSeasonRes = getRedisValue(DEFAULT_SEASON_KEY, MainSeasonRes.class).orElse(null);
        if (mainSeasonRes != null) {
            return DefaultSeasonRes.builder()
                .seasonDescription(mainSeasonRes.getSeasonDescription())
                .path(mainSeasonRes.getPath())
                .build();
        }

        return DefaultSeasonRes.builder()
            .seasonDescription("현재 기본 시즌이 설정되지 않았습니다.")
            .path(new ArrayList<>())
            .build();
    }


    @Override
    public void updateDefaultSeason(DefaultSeasonRes defaultSeasonRes) {
        MainSeasonRes mainSeasonRes = MainSeasonRes.builder()
            .seasonId(null)
            .seasonDescription(defaultSeasonRes.getSeasonDescription())
            .path(defaultSeasonRes.getPath())
            .build();
        redisTemplate.opsForValue().set(DEFAULT_SEASON_KEY, mainSeasonRes);
    }

    @Override
    public MainSeasonRes getActiveSeason() {
        Optional<LocalDateTime> SeasonStartDate = getRedisValue(SEASON_START_DATE_KEY,
            LocalDateTime.class);
        if (SeasonStartDate.isEmpty()) {
            updateNextSeasonDate();
        }

        if (SeasonStartDate.get().isBefore(LocalDateTime.now())) {
            return getRedisValue(ACTIVE_SEASON_KEY, MainSeasonRes.class).get();
        }

        return getRedisValue(DEFAULT_SEASON_KEY, MainSeasonRes.class)
            .orElse(MainSeasonRes.builder()
                .seasonDescription("메인 화면이 설정되지 않았습니다")
                .path(new ArrayList<>())
                .build());
    }

    private <T> Optional<T> getRedisValue(String key, Class<T> type) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key))
            ? Optional.ofNullable(type.cast(redisTemplate.opsForValue().get(key)))
            : Optional.empty();
    }

    /**
     * 시작일, 종료일 검증
     *
     * @param startedAt 시즌 시작일
     * @param endedAt   시즌 종료일
     */
    private void validateSeasonDates(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (startedAt.isAfter(endedAt)) {
            throw new InvalidDateOrderException("시작일은 종료일보다 앞서야 합니다.");
        }
    }

    /**
     * 시즌의 종료 일자에 맞추어 키를 레디스에 저장하는 메서드
     *
     * @param season 카드의 시즌 정보
     */
    private void setExpirationForNotification(Season season) {
        String redisKey = "season:expiration:" + season.getId();
        LocalDateTime getOpenedAt = season.getOpenedAt();
        Duration expirationDuration = Duration.between(LocalDateTime.now(), getOpenedAt);
        redisTemplate.opsForValue().set(redisKey, season.getName(), expirationDuration);
    }

    private void updateNextSeasonDate() {
        List<Season> seasonList = seasonRepository.findAll();
        LocalDateTime seasonStartDate = getRedisValue(SEASON_START_DATE_KEY,
            LocalDateTime.class).orElse(LocalDateTime.MAX);
        LocalDateTime now = LocalDateTime.now();

        Season nextSeason = null;
        for (Season season : seasonList) {
            if (season.getEndedAt().isAfter(now) && seasonStartDate.isAfter(
                season.getStartedAt())) {
                seasonStartDate = season.getStartedAt();
                nextSeason = season;
            }
        }

        if (nextSeason == null) {
            redisTemplate.opsForValue().set(SEASON_START_DATE_KEY, seasonStartDate);
            return;
        }

        Duration ttl = Duration.between(now, nextSeason.getEndedAt());
        MainSeasonRes mainSeasonRes = MainSeasonRes.builder()
            .seasonId(nextSeason.getId())
            .seasonDescription(nextSeason.getDescription())
            .path(nextSeason.getPath())
            .build();
        redisTemplate.opsForValue().set(SEASON_START_DATE_KEY, seasonStartDate, ttl);
        redisTemplate.opsForValue().set(ACTIVE_SEASON_KEY, mainSeasonRes, ttl);
    }
}
