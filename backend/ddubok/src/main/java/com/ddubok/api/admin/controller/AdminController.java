package com.ddubok.api.admin.controller;

import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.admin.service.AdminReportService;
import com.ddubok.api.admin.dto.response.GetReportListRes;
import com.ddubok.api.admin.service.MemberStatusService;
import com.ddubok.api.admin.service.SeasonService;
import com.ddubok.common.template.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 관리자가 신고 목록을 조회합니다.
     *
     * @param getReportListReq 필터링 조건
     * @return 신고 목록
     */
    @GetMapping("/reports")
    public BaseResponse<?> getAllReportList(
        @RequestBody GetReportListReq getReportListReq
    ) {
        List<GetReportListRes> reportList = adminReportService.getAllReportList(getReportListReq);
        return BaseResponse.ofSuccess(reportList);
    }

    /**
     * 관리자가 신고를 상세 조회합니다.
     *
     * @param  reportId 검색할 신고의 번호
     * @return 신고 내용에 대한 상세조회 반환
     */
    @GetMapping("/reports/{reportId}")
    public BaseResponse<?> getAllReportList(@PathVariable Long reportId) {
        GetReportDetailRes getReportDetailRes = adminReportService.getReportDetail(reportId);
        return BaseResponse.ofSuccess(getReportDetailRes);
    }
}
