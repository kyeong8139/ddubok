package com.ddubok.api.admin.service;

import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.admin.dto.response.GetReportListRes;
import java.util.List;

/**
 * 신고 관련 처리를 위한 서비스
 */
public interface AdminReportService {

    /**
     * 현재 모든 신고 목록을 조회한다.
     *
     * @param getReportListReq 신고 목록에 대한 필터링 정보를 담고 있는 Req
     * @return 모든 신고들을 반환한다.
     */
    List<GetReportListRes> getAllReportList(GetReportListReq getReportListReq);

    /**
     * 신고번호에 해당하는 신고의 상세정보를 조회한다.
     *
     * @param reportId 상세조회할 신고번호
     * @return 해당 신고의 상세정보를 반환한다.
     */
    GetReportDetailRes getReportDetail(Long reportId);

    /**
     * 신고번호에 해당하는 신고의 상태를 변경한다.
     *
     * @param reportId 변경할 신고번호
     * @param getReportListReq 수락,반려, 미처리등을 결정
     * @return 로직을 마치고 해당 신고의 신고번호, 신고제목, 상태를 반환한다.
     */
    GetReportListRes handleReport(Long reportId, GetReportListReq getReportListReq);
}
