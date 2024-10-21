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

    // 300 - card
    FAIL_TO_UPLOAD_FILE("300", "카드 이미지 업로드 실패");

    private String code;
    private String message;

}