package com.ddubok.common.template.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 200 - 성공
    OK("200", "성공"),
    CREATED("201", "생성됨"),
    DELETED("202", "삭제됨"),

    // 300 - S3
    FAIL_TO_UPLOAD_FILE("300", "이미지 업로드 실패"),

    // 400 - member
    MEMBER_NOT_FOUND("400", "멤버를 찾을 수 없음"),
    MEMBER_UPDATED("401", "수정됨"),
    SOCIAL_ACCESS_TOKEN_NOT_FOUND("402", "소셜 액세스 토큰을 찾을 수 없습니다."),
    INVALID_DELETE_MEMBER_ERROR("403", "연동 해제 중 문제가 발생하였습니다."),

    // 500 - season
    SEASON_NOT_FOUND("500", "시즌을 찾을 수 없음"),
    INVALID_DATE_ORDER("501", "시즌의 날짜 선정이 잘못 되었습니다."),
    INVALID_CARD_DATE("502", "시즌이 종료됨"),
    SEASON_INFO_NOT_FOUND("503", "메인 화면의 시즌 정보를 찾을 수 없음"),

    // 600 - card
    CARD_NOT_FOUND("600", "카드를 찾을 수 없음"),
    CARD_ALREADY_DELETED("601", "이미 삭제된 카드"),

    // 700 - album
    ALBUM_NOT_FOUND("700", "앨범을 찾을 수 없음"),
    ALBUM_ALREADY_DELETED("701", "이미 삭제된 앨범"),
    NO_ALBUM("702", "보유한 카드가 없음"),
    ALBUM_ALREADY_EXIST("703", "이미 받은 카드"),

    // 800 - auth
    INVALID_REFRESH_TOKEN("800", "Refresh 토큰 에러입니다."),
    PROVIDER_UNSUPPORTED("801", "해당 소셜 제공자는 지원하지 않음"),
    USER_TYPE_UNSUPPORTED("802", "해당 소셜 유저 타입을 지원하지 않음"),
    INVALID_ACCESS_TOKEN("803", "Access 토큰 에러입니다."),
    LOGIN_FAILED("804", "로그인에 실패하였습니다."),
    REFRESH_TOKEN_NOT_FOUND("805", "RefreshToken이 없습니다."),

    // 900 - admin
    INVALID_CONDITION("900", "조건이 올바르지 않음"),
    UNKNOWN_ROLE("901", "알수 없는 역할"),
    UNKNOWN_STATE("902", "알수 없는 상태"),
    
    // 1000 - report
    INVALID_TYPE("1000", "타입이 올바르지 않음"),
    REPORT_NOT_FOUND("1001", "신고를 찾을수 없음");

    private String code;
    private String message;

}