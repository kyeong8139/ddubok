package com.ddubok.api.report.entity;

import com.ddubok.api.admin.exception.InvalidConditionException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum State {

    ACCEPT("수락"),
    REJECT("반려"),
    UNPROCESSED("미처리");

    private final String name;

    State(String name) {
        this.name = name;
    }

    public String toName() {
        return this.name;
    }

    public static State fromName(String name) {
        return Arrays.stream(values())
            .filter(state -> state.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new InvalidConditionException("잘못된 상태 값입니다: " + name));
    }
}
