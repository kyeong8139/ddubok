package com.ddubok.common.template.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 200 - 성공
    OK("200", "성공"),
    CREATED("201", "생성됨"),
    DELETED("202", "삭제됨");

    private String code;
    private String message;

}