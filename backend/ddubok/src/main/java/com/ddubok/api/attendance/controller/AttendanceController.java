package com.ddubok.api.attendance.controller;

import com.ddubok.api.attendance.dto.response.GetAttendanceRes;
import com.ddubok.api.attendance.service.AttendanceService;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attendances")
public class AttendanceController {

    final private AttendanceService attendanceService;

    @GetMapping
    public BaseResponse<GetAttendanceRes> getAttendance() {

        /*
         * todo memberId를 실제 데이터로 변경
         */
        Long memberId = 2L;

        /*
         * todo maxAttendanceStreak 관련 로직 작성
         */
        List<LocalDate> attendanceList = attendanceService.getAttendanceList(memberId);

        GetAttendanceRes data = GetAttendanceRes.builder()
            .attendanceList(attendanceList)
            .attendanceCount(attendanceList.size())
            .build();

        return BaseResponse.ofSuccess(data);
    }
}