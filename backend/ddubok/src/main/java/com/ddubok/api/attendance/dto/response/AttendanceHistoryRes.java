package com.ddubok.api.attendance.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
@Builder
public class AttendanceHistoryRes {

    List<LocalDate> attendanceList;
    Integer attendanceCount;
}
