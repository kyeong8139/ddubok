package com.ddubok.api.attendance.controller;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.service.AttendanceService;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attendances")
public class AttendanceController {

    final private AttendanceService attendanceService;

    @GetMapping
    public BaseResponse<AttendanceHistoryRes> getAttendanceHistory() {

        /*
         * todo memberId를 실제 데이터로 변경
         */
        Long memberId = 1L;
        AttendanceHistoryRes res = attendanceService.getAttendanceHistoryThisMonth(memberId);

        return BaseResponse.ofSuccess(res);
    }

    @PostMapping
    public BaseResponse<CreateAttendanceRes> createAttendance() {

        /*
         * todo memberId를 실제 데이터로 변경
         */
        Long memberId = 1L;
        CreateAttendanceRes res = attendanceService.createAttendance(memberId);

        return BaseResponse.ofSuccess(res);
    }
}