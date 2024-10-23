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
     * @return 모든 신고들을 반환한다.
     */
    List<GetReportDetailRes> getAllReportList(GetReportListReq getReportListReq);

    /**
     * String 형태의 state를 enum으로 변경합니다.
     *
     * @param stateString
     * @return 형태에 맞는 eunm을 반환한다
     */
    State convertStringToState(String stateString);

    /**
     * enum 형태의 state를 String으로 변경합니다.
     *
     * @param state
     * @return 형태에 맞는 String을 반환한다
     */
    String convertStatetoString(State state);

    /**
     * 엔티티 형태의 값을 DTO로 변경
     *
     * @param reports
     * @return 엔티티를 GetReportDetailRes형태의 DTO로 변경
     */
    List<GetReportDetailRes> convertToDtoList(List<Report> reports);
}
