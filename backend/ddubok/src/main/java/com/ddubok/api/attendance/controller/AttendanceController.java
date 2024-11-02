package com.ddubok.api.attendance.controller;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.service.AttendanceService;
import com.ddubok.common.auth.util.AuthUtil;
import com.ddubok.common.template.response.BaseResponse;
import java.time.LocalDate;
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
    final private AuthUtil authUtil;

    @GetMapping
    public BaseResponse<AttendanceHistoryRes> getAttendanceHistory() {

        Long memberId = authUtil.getMemberId();
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        AttendanceHistoryRes res = attendanceService.getAttendanceHistoryThisMonth(memberId, year,
            month);

        return BaseResponse.ofSuccess(res);
    }

    @PostMapping
    public BaseResponse<CreateAttendanceRes> createAttendance() {

        Long memberId = authUtil.getMemberId();
        LocalDate currentDate = LocalDate.now();
        CreateAttendanceRes res = attendanceService.createAttendance(memberId, currentDate);

        return BaseResponse.ofSuccess(res);
    }
}