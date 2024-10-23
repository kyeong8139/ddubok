package com.ddubok.api.report.entity;

import com.ddubok.api.admin.exception.InvalidConditionException;
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
        for (State state : values()) {
            if (state.getName().equals(name)) {
                return state;
            }
        }
        throw new InvalidConditionException("잘못된 상태 값입니다: " + name);
    }
}
