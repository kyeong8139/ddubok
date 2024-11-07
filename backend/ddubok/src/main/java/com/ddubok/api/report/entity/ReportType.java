package com.ddubok.api.report.entity;

import com.ddubok.api.report.exception.InvalidTypeException;
import java.util.Arrays;

public enum ReportType {

    VILIFICATION("비속어"),
    ADVERTISMENT("광고"),
    OBSCENE("음란성 내용"),
    PERSONALINFORMATION("사생활 침해"),
    SPAM("도배"),
    ETC("기타");

    private final String typeName;

    ReportType(String typeName) {
        this.typeName = typeName;
    }

    public String toTypeName() {
        return this.typeName;
    }

    public static ReportType fromTypeName(String typeName) {
        return Arrays.stream(values())
            .filter(type -> type.toTypeName().equals(typeName))
            .findFirst()
            .orElseThrow(() -> new InvalidTypeException("잘못된 신고 사유 값입니다: " + typeName));
    }

}
