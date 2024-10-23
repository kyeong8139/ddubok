package com.ddubok.api.report.controller;

import com.ddubok.api.report.dto.request.ReportMemberReq;
import com.ddubok.api.report.dto.response.ReportMemberRes;
import com.ddubok.api.report.service.ReportService;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    final private ReportService reportService;

    /**
     * 사용자가 카드의 내용을 신고하는 경우 사용하는 API
     * @param reportMemberReq 신고할 멤버의 객체
     * @return 신고의 번호
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<?>> reportMember(
        @RequestBody ReportMemberReq reportMemberReq){
        Long reportId = reportService.reportMember(reportMemberReq);
        ReportMemberRes reportMemberRes = ReportMemberRes.builder()
            .reportId(reportId)
            .build();
        return ResponseEntity.ok(BaseResponse.ofSuccess(reportMemberRes));
    }
}
