package com.ddubok.spring.api.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.entity.Attendance;
import com.ddubok.api.attendance.entity.Fortune;
import com.ddubok.api.attendance.exception.IllegalDateException;
import com.ddubok.api.attendance.repository.AttendanceRepository;
import com.ddubok.api.attendance.repository.FortuneRepository;
import com.ddubok.api.attendance.service.AttendanceServiceImpl;
import com.ddubok.spring.BusinessLayerTestSupport;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
class AttendanceServiceTest extends BusinessLayerTestSupport {

    @Autowired
    private AttendanceServiceImpl attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private FortuneRepository fortuneRepository;

    @AfterEach
    void tearDown() {
        fortuneRepository.deleteAll();
        attendanceRepository.deleteAllInBatch();
    }

    @DisplayName("유효한 월 입력 시 해당 달의 출석 기록을 조회할 수 있다")
    @Test
    void getAttendanceHistoryThisMonthWithAppropriateDate() {
        // given
        LocalDate date1 = LocalDate.of(2024, 10, 29);
        LocalDate date2 = LocalDate.of(2024, 10, 30);
        LocalDate date3 = LocalDate.of(2024, 10, 31);
        Attendance attendance1 = Attendance.builder()
            .member(testMember)
            .date(date1)
            .build();
        Attendance attendance2 = Attendance.builder()
            .member(testMember)
            .date(date2)
            .build();
        Attendance attendance3 = Attendance.builder()
            .member(testMember)
            .date(date3)
            .build();
        attendanceRepository.saveAll(List.of(attendance1, attendance2, attendance3));
        // when
        AttendanceHistoryRes results = attendanceService.getAttendanceHistoryThisMonth(
            testMember.getId(), 2024, 10);

        // then
        assertThat(results)
            .isNotNull()
            .satisfies(response -> {
                assertThat(response.getAttendanceList())
                    .hasSize(3)
                    .containsExactly(date1, date2, date3);
                assertThat(response.getAttendanceCount())
                    .isEqualTo(3);
            });
    }

    @DisplayName("출석 기록이 없는 달 조회 시 빈 리스트와 출석 수 0을 반환한다")
    @Test
    void getAttendanceHistoryThisMonthWithoutData() {
        // given

        // when
        AttendanceHistoryRes results = attendanceService.getAttendanceHistoryThisMonth(
            testMember.getId(), 2024, 10);

        // then
        assertThat(results)
            .isNotNull()
            .satisfies(response -> {
                assertThat(response.getAttendanceList())
                    .hasSize(0)
                    .isEmpty();
                assertThat(response.getAttendanceCount())
                    .isEqualTo(0);
            });
    }

    @DisplayName("월이 1 미만 또는 12 초과일 때 IllegalDateException이 발생한다")
    @Test
    void getAttendanceHistoryThisMonthWithIllegalDate() {
        // given
        int invalidMonth = 13;
        LocalDate date1 = LocalDate.of(2024, 10, 29);
        LocalDate date2 = LocalDate.of(2024, 10, 30);
        LocalDate date3 = LocalDate.of(2024, 10, 31);
        Attendance attendance1 = Attendance.builder()
            .member(testMember)
            .date(date1)
            .build();
        Attendance attendance2 = Attendance.builder()
            .member(testMember)
            .date(date2)
            .build();
        Attendance attendance3 = Attendance.builder()
            .member(testMember)
            .date(date3)
            .build();
        attendanceRepository.saveAll(List.of(attendance1, attendance2, attendance3));

        // when      // then
        assertThatThrownBy(
            () -> attendanceService.getAttendanceHistoryThisMonth(testMember.getId(), 2024,
                invalidMonth))
            .isInstanceOf(IllegalDateException.class)
            .hasMessage("월은 1에서 12 사이의 값이어야 합니다.");
    }

    @DisplayName("Redis에 키가 없으면 출석 정보를 저장하고 Redis에 캐시한다")
    @Test
    void createAttendanceWhenRedisKeyNotExists() {
        // given
        LocalDate beforeDate1 = LocalDate.of(2024, 10, 28);
        LocalDate beforeDate2 = LocalDate.of(2024, 10, 29);
        LocalDate beforeDate3 = LocalDate.of(2024, 10, 30);
        Attendance attendance1 = Attendance.builder()
            .member(testMember)
            .date(beforeDate1)
            .build();
        Attendance attendance2 = Attendance.builder()
            .member(testMember)
            .date(beforeDate2)
            .build();
        Attendance attendance3 = Attendance.builder()
            .member(testMember)
            .date(beforeDate3)
            .build();
        attendanceRepository.saveAll(List.of(attendance1, attendance2, attendance3));
        LocalDate currentDate = LocalDate.of(2024, 10, 31);
        when(redisTemplate.hasKey(anyString())).thenReturn(false);

        Fortune fortune1 = Fortune.builder().sentence("운세내용1").build();
        Fortune fortune2 = Fortune.builder().sentence("운세내용2").build();
        Fortune fortune3 = Fortune.builder().sentence("운세내용3").build();
        fortuneRepository.saveAll(List.of(
            fortune1, fortune2, fortune3
        ));

        // when
        CreateAttendanceRes results = attendanceService.createAttendance(testMember.getId(),
            currentDate);

        // then
        assertThat(results)
            .isNotNull()
            .satisfies(response -> {
                assertThat(response.getFortune().getSentence())
                    .isIn(fortune1.getSentence(), fortune2.getSentence(), fortune3.getSentence());
                assertThat(response.getFortune().getScore())
                    .isGreaterThanOrEqualTo(60);
                assertThat(response.getAttendanceHistory().getAttendanceList()).hasSize(
                    results.getAttendanceHistory().getAttendanceCount());
            });
    }

    @DisplayName("출석 기록을 이미 한 날에 다시 출석하면 동일한 정보를 반환한다")
    @Test
    void createAttendanceWhenRedisKeyExists() {
        // given
        Fortune fortune1 = Fortune.builder().sentence("운세내용1").build();
        Fortune fortune2 = Fortune.builder().sentence("운세내용2").build();
        Fortune fortune3 = Fortune.builder().sentence("운세내용3").build();
        fortuneRepository.saveAll(List.of(
            fortune1, fortune2, fortune3
        ));
        LocalDate currentDate = LocalDate.of(2024, 10, 31);
        CreateAttendanceRes existingAttendance = attendanceService.createAttendance(
            testMember.getId(), currentDate);

        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(redisTemplate.opsForValue().get(anyString())).thenReturn(existingAttendance);

        // when
        CreateAttendanceRes result = attendanceService.createAttendance(testMember.getId(),
            currentDate);
        // then
        assertThat(result).isEqualTo(existingAttendance);
    }

    @DisplayName("운세와 점수가 랜덤하게 선택되는지 검증한다")
    @Test
    void verifyFortuneIsRandomlySelected() {
        // given
        LocalDate currentDate = LocalDate.of(2024, 10, 31);
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        Fortune fortune1 = Fortune.builder().sentence("운세내용1").build();
        Fortune fortune2 = Fortune.builder().sentence("운세내용2").build();
        Fortune fortune3 = Fortune.builder().sentence("운세내용3").build();
        Fortune fortune4 = Fortune.builder().sentence("운세내용4").build();
        Fortune fortune5 = Fortune.builder().sentence("운세내용5").build();
        Fortune fortune6 = Fortune.builder().sentence("운세내용6").build();
        Fortune fortune7 = Fortune.builder().sentence("운세내용7").build();
        Fortune fortune8 = Fortune.builder().sentence("운세내용8").build();
        Fortune fortune9 = Fortune.builder().sentence("운세내용9").build();
        Fortune fortune10 = Fortune.builder().sentence("운세내용10").build();
        fortuneRepository.saveAll(List.of(
            fortune1, fortune2, fortune3, fortune4, fortune5, fortune6, fortune7,
            fortune8, fortune9, fortune10
        ));

        // when
        HashSet<String> set1 = new HashSet<>();
        HashSet<Integer> set2 = new HashSet<>();
        for (int i = 0; i < 30; ++i) {
            CreateAttendanceRes results = attendanceService.createAttendance(
                testMember.getId(), currentDate);
            set1.add(results.getFortune().getSentence());
            set2.add(results.getFortune().getScore());
        }

        // then
        assertThat(set1.size()).isGreaterThan(2);
        assertThat(set2.size()).isGreaterThan(2);
    }
}