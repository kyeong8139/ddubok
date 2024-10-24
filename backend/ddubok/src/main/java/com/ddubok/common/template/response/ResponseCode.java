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
    FAIL_TO_UPLOAD_FILE("300", "카드 이미지 업로드 실패"),

    // 400 - member
    MEMBER_NOT_FOUND("400", "멤버를 찾을 수 없음"),
    MEMBER_UPDATED("401", "수정됨"),

    // 500 - season
    SEASON_NOT_FOUND("500", "시즌을 찾을 수 없음"),

    // 600 - card
    CARD_NOT_FOUND("600", "카드를 찾을 수 없음"),
    CARD_ALREADY_DELETED("601", "이미 삭제된 카드"),

    // 700 - album
    ALBUM_NOT_FOUND("700", "앨범을 찾을 수 없음"),
    ALBUM_ALREADY_DELETED("701", "이미 삭제된 앨범"),
    NO_ALBUM("702", "보유한 카드가 없음"),

    // 800 - auth
    INVALID_REFRESH_TOKEN("800", "Refresh 토큰을 찾을 수 없음"),
    RT_EXPIRED("801", "Refresh 토큰이 만료됨"),
    PROVIDER_UNSUPPORTED("802", "해당 소셜 제공자는 지원하지 않음"),
    USER_TYPE_UNSUPPORTED("803", "해당 소셜 유저 타입을 지원하지 않음"),

    // 900 - admin
    INVALID_CONDITION("900", "조건이 올바르지 않음");

    private String code;
    private String message;

}