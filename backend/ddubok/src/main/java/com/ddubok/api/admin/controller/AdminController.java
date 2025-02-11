package com.ddubok.api.admin.controller;

import com.ddubok.api.admin.dto.request.CreateSeasonReq;
import com.ddubok.api.admin.dto.request.CreateSeasonReqDto;
import com.ddubok.api.admin.dto.request.DefaultSeasonReq;
import com.ddubok.api.admin.dto.request.GetMemberListReq;
import com.ddubok.api.admin.dto.request.GetReportListReq;
import com.ddubok.api.admin.dto.request.HandleReportReq;
import com.ddubok.api.admin.dto.request.UpdateSeasonReq;
import com.ddubok.api.admin.dto.request.UpdateSeasonReqDto;
import com.ddubok.api.admin.dto.response.CreateSeasonRes;
import com.ddubok.api.admin.dto.response.DefaultSeasonRes;
import com.ddubok.api.admin.dto.response.GetMemberDetailRes;
import com.ddubok.api.admin.dto.response.GetMemberListRes;
import com.ddubok.api.admin.dto.response.GetReportDetailRes;
import com.ddubok.api.admin.dto.response.GetSeasonDetailRes;
import com.ddubok.api.admin.dto.response.GetSeasonListRes;
import com.ddubok.api.admin.dto.response.UpdateMemberRoleRes;
import com.ddubok.api.admin.dto.response.UpdateMemberStateRes;
import com.ddubok.api.admin.dto.response.UpdateSeasonRes;
import com.ddubok.api.admin.service.AdminReportService;
import com.ddubok.api.admin.dto.response.GetReportListRes;
import com.ddubok.api.admin.service.MemberStatusService;
import com.ddubok.api.admin.service.SeasonService;
import com.ddubok.common.s3.S3ImageService;
import com.ddubok.common.s3.dto.FileMetaInfo;
import com.ddubok.common.template.response.BaseResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    final private SeasonService seasonService;
    final private MemberStatusService memberStatusService;
    final private AdminReportService adminReportService;
    final private S3ImageService s3ImageService;

    /**
     * 관리자가 신고 목록을 조회합니다.
     *
     * @param getReportListReq 필터링 조건
     * @return 신고 목록
     */
    @PostMapping("/reports")
    public BaseResponse<?> getAllReportList(
        @RequestBody GetReportListReq getReportListReq
    ) {
        List<GetReportListRes> reportList = adminReportService.getAllReportList(getReportListReq);
        return BaseResponse.ofSuccess(reportList);
    }

    /**
     * 관리자가 신고를 상세 조회합니다.
     *
     * @param reportId 검색할 신고의 번호
     * @return 신고 내용에 대한 상세조회 반환
     */
    @GetMapping("/reports/{reportId}")
    public BaseResponse<?> getReportDetail(@PathVariable Long reportId) {
        GetReportDetailRes getReportDetailRes = adminReportService.getReportDetail(reportId);
        return BaseResponse.ofSuccess(getReportDetailRes);
    }

    /*
     * todo : 신고 ip 차단 및 AI 학습 자료로 활용예정
     */

    /**
     * 관리자가 신고를 처리합니다.
     *
     * @param reportId 검색할 신고의 번호
     * @return 신고번호와 제목 상태변화를 반환
     */
    @PatchMapping("/reports/{reportId}")
    public BaseResponse<?> getReportDetail(
        @PathVariable Long reportId, @RequestBody HandleReportReq handleReportReq) {
        GetReportListRes getReportState = adminReportService.handleReport(reportId,
            handleReportReq);
        return BaseResponse.ofSuccess(getReportState);
    }

    /**
     * 관리자가 사용자 목록을 조회합니다.
     *
     * @param getMemberListReq 필터링 및 검색 조건
     * @return 사용자 목록
     */
    @PostMapping("/members")
    public BaseResponse<?> getAllMemberList(
        @RequestBody GetMemberListReq getMemberListReq
    ) {
        List<GetMemberListRes> memberList = memberStatusService.getMemberList(getMemberListReq);
        return BaseResponse.ofSuccess(memberList);
    }

    /**
     * 관리자가 사용자를 상세 조회합니다.
     *
     * @param memberId 상세조회할 사용자의 번호
     * @return 해당 사용자의 정보를 반환한다.
     */
    @GetMapping("/members/{memberId}")
    public BaseResponse<?> getMemberDetail(
        @PathVariable Long memberId
    ) {
        GetMemberDetailRes memberDetail = memberStatusService.getMemberDetail(memberId);
        return BaseResponse.ofSuccess(memberDetail);
    }

    /**
     * 관리자가 사용자의 역할을 변경합니다.
     *
     * @param memberId 역할을 변경할 사용자의 번호
     * @return 해당 사용자의 번호와 변경된 역할
     */
    @PatchMapping("/members/role/{memberId}")
    public BaseResponse<?> updateMemberRole(
        @PathVariable Long memberId
    ) {
        UpdateMemberRoleRes updateMemberRoleRes = memberStatusService.updateMemberRole(memberId);
        return BaseResponse.ofSuccess(updateMemberRoleRes);
    }

    /**
     * 관리자가 사용자의 상태를 변경합니다.
     *
     * @param memberId 상태를 변경할 사용자의 번호
     * @return 해당 사용자의 번호와 변경된 상태
     */
    @PatchMapping("/members/state/{memberId}")
    public BaseResponse<?> updateMemberState(
        @PathVariable Long memberId
    ) {
        UpdateMemberStateRes updateMemberStateRes = memberStatusService.updateMemberState(memberId);
        return BaseResponse.ofSuccess(updateMemberStateRes);
    }

    /**
     * 관리자가 시즌을 등록합니다.
     *
     * @param images          배너에 활용될 이미지 리스트
     * @param createSeasonReq 생성될 시즌의 정보를 갖고 있는 객체
     */
    @PostMapping("/seasons")
    public BaseResponse<?> createSeason(
        @RequestPart(name = "image") List<MultipartFile> images,
        @RequestPart CreateSeasonReq createSeasonReq
    ) {
        List<String> paths = images.stream()
            .map(image -> s3ImageService.uploadBannerImg(image))
            .map(FileMetaInfo::getUrl)
            .collect(Collectors.toList());
        CreateSeasonRes seasonId = seasonService.createSeason(
            CreateSeasonReqDto.builder()
                .seasonName(createSeasonReq.getName())
                .seasonDescription(createSeasonReq.getSeasonDescription())
                .startedAt(createSeasonReq.getStartedAt())
                .endedAt(createSeasonReq.getEndedAt())
                .openedAt(createSeasonReq.getOpenedAt())
                .path(paths)
                .build()
        );
        return BaseResponse.ofSuccess(seasonId);
    }

    /**
     * 시즌의 상세정보를 조회합니다.
     *
     * @param seasonId 현재 시즌의 값
     */
    @GetMapping("/seasons/{seasonId}")
    public BaseResponse<?> getSeasonDetail(
        @PathVariable Long seasonId
    ) {
        GetSeasonDetailRes getSeasonDetailRes = seasonService.getSeasonDetail(seasonId);
        return BaseResponse.ofSuccess(getSeasonDetailRes);
    }

    /**
     * 시즌을 전체 목록을 조회합니다.
     */
    @GetMapping("/seasons")
    public BaseResponse<?> getSeasonList() {
        List<GetSeasonListRes> getSeasonListRes = seasonService.getSeasonList();
        return BaseResponse.ofSuccess(getSeasonListRes);
    }

    /**
     * 관리자가 시즌을 변경합니다.
     *
     * @param seasonId        수정할 시즌의 ID
     * @param images          배너에 활용될 이미지 리스트
     * @param updateSeasonReq 수정될 시즌의 정보를 갖고 있는 객체
     */
    @PutMapping("/seasons/{seasonId}")
    public BaseResponse<?> updateSeason(
        @PathVariable Long seasonId,  // PathVariable로 시즌 ID를 받음
        @RequestPart(name = "image") List<MultipartFile> images,
        @RequestPart UpdateSeasonReq updateSeasonReq
    ) {
        List<String> paths = images.stream()
            .map(image -> s3ImageService.uploadBannerImg(image))
            .map(FileMetaInfo::getUrl)
            .collect(Collectors.toList());

        UpdateSeasonRes updateSeasonId = seasonService.updateSeason(seasonId,
            UpdateSeasonReqDto.builder()
                .seasonName(updateSeasonReq.getName())
                .seasonDescription(updateSeasonReq.getSeasonDescription())
                .startedAt(updateSeasonReq.getStartedAt())
                .endedAt(updateSeasonReq.getEndedAt())
                .openedAt(updateSeasonReq.getOpenedAt())
                .path(paths)
                .build()
        );
        return BaseResponse.ofSuccess(updateSeasonId);
    }

    /**
     * 관리자가 기본 메인 화면 정보를 가지고 옵니다.
     *
     * @return 기본 메인 정보를 갖고 있는 객체
     */
    @GetMapping("/seasons/default")
    public BaseResponse<?> getDefaultSeason() {
        return BaseResponse.ofSuccess(seasonService.getDefaultSeason());
    }

    /**
     * 관리자가 기본 메인 화면을 설정합니다.
     *
     * @param images           배너에 활용될 이미지 리스트
     * @param defaultSeasonReq 기본 메인 정보를 갖고 있는 객체
     */
    @PostMapping("/seasons/default")
    public BaseResponse<?> updateDefaultSeason(
        @RequestPart(name = "image") List<MultipartFile> images,
        @RequestPart DefaultSeasonReq defaultSeasonReq
    ) {
        List<String> paths = images.stream()
            .map(image -> s3ImageService.uploadBannerImg(image))
            .map(FileMetaInfo::getUrl)
            .collect(Collectors.toList());

        DefaultSeasonRes defaultSeasonRes = DefaultSeasonRes.builder()
            .seasonDescription(defaultSeasonReq.getSeasonDescription())
            .path(paths)
            .build();

        seasonService.updateDefaultSeason(defaultSeasonRes);

        return BaseResponse.ofSuccess(defaultSeasonRes);
    }
}
