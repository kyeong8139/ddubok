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

    // 500 - season
    SEASON_NOT_FOUND("500", "시즌을 찾을 수 없음"),

    // 600 - card
    CARD_NOT_FOUND("600", "카드를 찾을 수 없음"),

    // 700 - album
    ALBUM_NOT_FOUND("700", "앨범을 찾을 수 없음"),
    ALBUM_ALREADY_DELETED("701", "이미 삭제된 앨범");

    private String code;
    private String message;

}