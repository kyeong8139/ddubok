package com.ddubok.api.attendance.service;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    /**
     * 특정 유저가 이번달에 출석한 일자의 리스트를 반환함
     *
     * @param memberId 유저의 id
     * @return 유저가 이번달에 출석한 일자의 리스트
     */
    List<LocalDate> getAttendanceList(Long memberId);
}
