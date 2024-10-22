package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;

public interface AttendanceService {

    /**
     * 특정 유저의 이번달 출석 기록을 반환함
     *
     * @param memberId 유저의 id값
     * @return 유저의 이번달 출석 기록 객체
     */
    AttendanceHistoryRes getAttendanceHistoryThisMonth(Long memberId);
}
