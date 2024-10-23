package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.entity.State;
import java.util.List;

/**
 * 신고 관련 처리를 위한 서비스
 */
public interface AdminReportService {

    /**
     * 현재 모든 신고 목록을 조회한다.
     *
     * @param getReportListReq
     * @return 모든 신고들을 반환한다.
     */
    List<GetReportDetailRes> getAllReportList(GetReportListReq getReportListReq);
}
