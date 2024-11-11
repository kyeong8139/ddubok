package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import java.time.LocalDate;

public interface AttendanceService {

    /**
     * 특정 유저의 이번달 출석 기록을 반환함
     *
     * @param memberId 유저의 id값
     * @param year     조회하고 싶은 년도의 값
     * @param month    조회하고 싶은 월의 값
     * @return 유저의 이번달 출석 기록 객체
     */
    AttendanceHistoryRes getAttendanceHistoryThisMonth(Long memberId, int year, int month);

    /**
     * 유저의 오늘자 출석 기록을 생성함
     *
     * @param memberId 유저의 id값
     * @param currentDate 출석하는 날짜
     * @return 출석체크 결과 객체 (운세,)
     */
    CreateAttendanceRes createAttendance(Long memberId, LocalDate currentDate);

    /**
     * 출석체크 알림 전송
     */
    void sendAttendanceNotification();
}
