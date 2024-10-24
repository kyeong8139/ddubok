package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.admin.dto.response.GetReportListRes;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.entity.State;
import com.ddubok.api.report.exception.ReportNotFoundException;
import com.ddubok.api.report.repository.ReportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

    private final ReportRepository reportRepository;

    /**
     * @inheritDoc
     */
    @Override
    public List<GetReportListRes> getAllReportList(GetReportListReq getReportListReq) {
        String stateString = getReportListReq.getState();
        List<Report> reports = new ArrayList<>();

        if (stateString == null) {
            reports = reportRepository.findAll();
        }
        if (stateString != null) {
            State state = State.fromName(stateString);
            reports = reportRepository.findByState(state);
        }

        return reports.stream()
            .map(report -> GetReportListRes.builder()
                .id(report.getId())
                .title(report.getTitle())
                .state(report.getState().toName())  // Enum을 문자열로 변환
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public GetReportDetailRes getReportDetail(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
            () -> new ReportNotFoundException("report not found: " + reportId)
        );
        return GetReportDetailRes.builder()
            .title(report.getTitle())
            .type(report.getType().toTypeName())
            .cardId(report.getCard().getId())
            .content(report.getContent())
            .build();
    }
}
