package com.ddubok.api.admin.controller;

import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.service.AdminReportService;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.admin.service.MemberStatusService;
import com.ddubok.api.admin.service.SeasonService;
import com.ddubok.common.template.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    final private SeasonService seasonService;
    final private MemberStatusService memberStatusService;
    final private AdminReportService adminReportService;

    /**
     * 관리자가 신고를 조회합니다.
     *
     * @param getReportListReq 필터링 조건
     * @return 신고 목록
     */
    @GetMapping("/reports")
    public BaseResponse<?> getAllReportList(
        @RequestBody GetReportListReq getReportListReq
    ) {
        List<GetReportDetailRes> reportList = adminReportService.getAllReportList(getReportListReq);
        return BaseResponse.ofSuccess(reportList);
    }
}
