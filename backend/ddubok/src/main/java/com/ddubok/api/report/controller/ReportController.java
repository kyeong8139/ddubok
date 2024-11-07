package com.ddubok.api.report.controller;

import com.ddubok.api.report.dto.request.ReportCardReq;
import com.ddubok.api.report.dto.response.ReportCardRes;
import com.ddubok.api.report.service.ReportService;
import com.ddubok.common.auth.util.AuthUtil;
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
    private final AuthUtil authUtil;


    /**
     * 사용자가 카드의 내용을 신고하는 경우 사용하는 API
     * @param reportCardReq 신고할 멤버의 객체
     * @return 신고의 번호
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse<?>> reportMember(
        @RequestBody ReportCardReq reportCardReq){
        Long memberId = authUtil.getMemberId();
        ReportCardRes reportCardRes = ReportCardRes.builder()
            .reportId(reportService.reportCard(memberId, reportCardReq).getId())
            .build();
        return ResponseEntity.ok(BaseResponse.ofSuccess(reportCardRes));
    }
}
