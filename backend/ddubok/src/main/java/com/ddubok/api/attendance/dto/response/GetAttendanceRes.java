package com.ddubok.api.attendance.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
@Builder
public class GetAttendanceRes {

    List<LocalDate> attendanceList;
    int attendanceCount;
    int maxAttendanceStreak;
}
