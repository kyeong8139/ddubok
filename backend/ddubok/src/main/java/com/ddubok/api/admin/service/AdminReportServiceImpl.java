package com.ddubok.api.admin.service;

import static com.ddubok.api.report.entity.State.ACCEPT;
import static com.ddubok.api.report.entity.State.REJECT;
import static com.ddubok.api.report.entity.State.UNPROCESSED;

import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.admin.exception.InvalidConditionException;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.entity.State;
import com.ddubok.api.report.repository.ReportRepository;
import java.util.ArrayList;
import java.util.List;
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
    public List<GetReportDetailRes> getAllReportList(GetReportListReq getReportListReq) {
        String stateString = getReportListReq.getState();
        List<Report> reports = new ArrayList<>();

        if (stateString == null) {
            reports = reportRepository.findAll();
        }
        if (stateString != null) {
            State state = convertStringToState(stateString);
            reports = reportRepository.findByState(state);
        }
        return convertToDtoList(reports);
    }

    /**
     * @inheritDoc
     */
    @Override
    public State convertStringToState(String stateString) {
        switch (stateString) {
            case "미처리":
                return UNPROCESSED;
            case "수락":
                return ACCEPT;
            case "반려":
                return REJECT;
            default:
                throw new InvalidConditionException("잘못된 상태 값입니다: " + stateString);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public String convertStatetoString(State state) {
        switch (state) {
            case ACCEPT:
                return "수락";
            case REJECT:
                return "반려";
            case UNPROCESSED:
                return "미처리";
            default:
                return state.toString();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<GetReportDetailRes> convertToDtoList(List<Report> reports) {
        List<GetReportDetailRes> result = new ArrayList<>();
        for (Report report : reports) {
            result.add(GetReportDetailRes.builder()
                .id(report.getId())
                .title(report.getTitle())
                .state(convertStatetoString(report.getState()))
                .build());
        }
        return result;
    }
}
