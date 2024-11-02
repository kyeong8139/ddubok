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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable pageable = PageRequest.of(getReportListReq.getPage(), getReportListReq.getSize(), Sort.by("id").descending());
        Page<Report> reports = null;

        if (stateString == null) {
            reports = reportRepository.findAll(pageable);
        }
        if (stateString != null) {
            State state = State.fromName(stateString);
            reports = reportRepository.findByState(state, pageable);
        }

        return reports.stream()
            .map(report -> GetReportListRes.builder()
                .id(report.getId())
                .title(report.getTitle())
                .state(report.getState().toName())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public GetReportDetailRes getReportDetail(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
            () -> new ReportNotFoundException("report not found: " + reportId)
        );
        return GetReportDetailRes.builder()
            .title(report.getTitle())
            .reportType(report.getReportType().toTypeName())
            .cardId(report.getCard().getId())
            .content(report.getContent())
            .build();
    }

    /**
     * @inheritDoc
     */
    @Override
    public GetReportListRes handleReport(Long reportId, GetReportListReq getReportListReq) {
        Report report = reportRepository.findById(reportId).orElseThrow(
            () -> new ReportNotFoundException("report not found: " + reportId)
        );
        report.updateReportState(State.fromName(getReportListReq.getState()));
        return GetReportListRes.builder()
            .id(reportId)
            .title(report.getTitle())
            .state(report.getState().toName())
            .build();
    }
}
