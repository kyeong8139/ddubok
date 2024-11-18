package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.CreateSeasonReqDto;
import com.ddubok.api.admin.dto.request.SaveSeasonReq;
import com.ddubok.api.admin.dto.request.UpdateSeasonReqDto;
import com.ddubok.api.admin.dto.response.MainSeasonRes;
import com.ddubok.api.admin.dto.response.CreateSeasonRes;
import com.ddubok.api.admin.dto.response.DefaultSeasonRes;
import com.ddubok.api.admin.dto.response.GetSeasonDetailRes;
import com.ddubok.api.admin.dto.response.GetSeasonListRes;
import com.ddubok.api.admin.dto.response.UpdateSeasonRes;
import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.exception.InvalidDateOrderException;
import com.ddubok.api.admin.exception.SeasonInfoNotFoundException;
import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.admin.repository.SeasonRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
        SaveSeasonReq saveSeasonReq = getRedisValue(DEFAULT_SEASON_KEY, SaveSeasonReq.class);
        if (saveSeasonReq == null) {
            return DefaultSeasonRes.builder()
                .seasonDescription("현재 기본 시즌이 설정되지 않았습니다.")
                .path(new ArrayList<>())
                .build();
        }

        return DefaultSeasonRes.builder()
            .seasonDescription(saveSeasonReq.getSeasonDescription())
            .path(saveSeasonReq.getPath())
            .build();
    }


    @Override
    public void updateDefaultSeason(DefaultSeasonRes defaultSeasonRes) {
        SaveSeasonReq saveSeasonReq = SaveSeasonReq.builder()
            .seasonId(null)
            .seasonDescription(defaultSeasonRes.getSeasonDescription())
            .path(defaultSeasonRes.getPath())
            .build();
        redisTemplate.opsForValue().set(DEFAULT_SEASON_KEY, saveSeasonReq);
    }

    @Override
    public MainSeasonRes getActiveSeason() {
        LocalDateTime seasonStartDate = getRedisValue(SEASON_START_DATE_KEY, LocalDateTime.class);
        if (seasonStartDate == null) {
            seasonStartDate = updateNextSeasonDate();
        }

        SaveSeasonReq saveSeasonReq = getRedisValue(DEFAULT_SEASON_KEY, SaveSeasonReq.class);
        if (seasonStartDate.isBefore(LocalDateTime.now())) {
            saveSeasonReq = getRedisValue(ACTIVE_SEASON_KEY, SaveSeasonReq.class);
        }

        if (saveSeasonReq == null) {
            throw new SeasonInfoNotFoundException();
        }

        return MainSeasonRes.builder()
            .seasonId(saveSeasonReq.getSeasonId())
            .seasonDescription(saveSeasonReq.getSeasonDescription())
            .path(saveSeasonReq.getPath())
            .build();
    }

    /**
     * 레디스에서 데이터를 가져오는 메서드
     *
     * @param key 레디스에 저장된 키
     * @param type 변환할 타입
     *
     * @return 레디스에 저장된 값
     */
    private <T> T getRedisValue(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);

        if (type == LocalDateTime.class && value instanceof String) {
            return type.cast(LocalDateTime.parse((String) value, FORMATTER));
        }
        return type.cast(value);
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

    /**
     * 다음에 시작될 시즌 정보를 갱신하는 메서드
     *
     * @return 다음에 시작될 시즌의 시작 일자
     */
    private LocalDateTime updateNextSeasonDate() {
        List<Season> seasonList = seasonRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime seasonStartDate = LocalDateTime.MAX;

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
            return seasonStartDate;
        }

        Duration ttl = Duration.between(now, nextSeason.getEndedAt());
        ArrayList<String> seasonPath = new ArrayList<>(nextSeason.getPath());

        SaveSeasonReq saveSeasonReq = SaveSeasonReq.builder()
            .seasonId(nextSeason.getId())
            .seasonDescription(nextSeason.getDescription())
            .path(seasonPath)
            .build();
        redisTemplate.opsForValue().set(SEASON_START_DATE_KEY, seasonStartDate, ttl);
        redisTemplate.opsForValue().set(ACTIVE_SEASON_KEY, saveSeasonReq, ttl);
        return seasonStartDate;
    }
}
