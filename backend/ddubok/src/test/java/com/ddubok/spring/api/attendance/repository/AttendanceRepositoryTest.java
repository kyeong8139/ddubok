package com.ddubok.spring.api.attendance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddubok.api.attendance.entity.Attendance;
import com.ddubok.api.attendance.repository.AttendanceRepository;
import com.ddubok.spring.PersistenceLayerTestSupport;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
class AttendanceRepositoryTest extends PersistenceLayerTestSupport {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @DisplayName("출석 기록이 있을 때, 특정 멤버와 날짜에 해당하는 데이터를 조회할 수 있다")
    @Test
    void getDateByMemberIdAndMonthWithData() {
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
        int year = 2024;
        int month = 10;

        // when
        List<LocalDate> result = attendanceRepository.getDateByMemberIdAndMonth(testMember.getId(),
            2024, 10);

        // then
        assertThat(result).hasSize(3)
            .contains(date1, date2, date3);
    }

    @DisplayName("출석 기록이 있을 때, 요청한 달의 데이터만 조회되며 다른 달의 데이터는 조회되지 않는다")
    @Test
    void getAttendanceByMemberIdAndMonth_ReturnsOnlyRecordsForRequestedMonth() {
        // given
        LocalDate date1 = LocalDate.of(2024, 10, 29);
        LocalDate date2 = LocalDate.of(2024, 10, 30);
        LocalDate date3 = LocalDate.of(2024, 10, 31);
        LocalDate date4 = LocalDate.of(2024, 9, 30);
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
        Attendance attendance4 = Attendance.builder()
            .member(testMember)
            .date(date4)
            .build();
        attendanceRepository.saveAll(List.of(attendance1, attendance2, attendance3, attendance4));
        int year = 2024;
        int month = 10;

        // when
        List<LocalDate> result = attendanceRepository.getDateByMemberIdAndMonth(testMember.getId(),
            2024, 10);

        // then
        assertThat(result).hasSize(3)
            .contains(date1, date2, date3)
            .doesNotContain(date4);
    }

    @DisplayName("출석 기록이 없을 때, 특정 멤버와 날짜에 대한 조회 시 빈 결과를 반환한다")
    @Test
    void getDateByMemberIdAndMonthWithoutData() {
        // given
        int year = 2024;
        int month = 10;

        // when
        List<LocalDate> result = attendanceRepository.getDateByMemberIdAndMonth(testMember.getId(),
            2024, 10);

        // then
        assertThat(result).hasSize(0);
    }

}