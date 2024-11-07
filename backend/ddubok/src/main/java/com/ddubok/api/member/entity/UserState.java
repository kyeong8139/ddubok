package com.ddubok.api.member.entity;

import com.ddubok.api.admin.exception.InvalidConditionException;
import com.ddubok.api.report.entity.State;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum UserState {

    ACTIVATED("활성"),
    INACTIVATED("비활성"),
    BANNED("정지");

    private final String userStateName;

    UserState(String userStateName) {
        this.userStateName = userStateName;
    }

    public String toUserStateName() {
        return this.userStateName;
    }

    public static UserState fromUserStateName(String userStateName) {
        return Arrays.stream(values())
            .filter(UserState -> UserState.getUserStateName().equals(userStateName))
            .findFirst()
            .orElseThrow(() -> new InvalidConditionException("잘못된 상태 값입니다: " + userStateName));
    }
}