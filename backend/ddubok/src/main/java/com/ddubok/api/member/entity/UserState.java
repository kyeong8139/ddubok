package com.ddubok.api.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserState {
    
    ACTIVATED("활성"),
    INACTIVATED("비활성"),
    BANNED("차단");

    private final String value;

}