package com.ddubok.api.member.entity;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER("사용자"),
    ROLE_ADMIN("관리자"),
    ROLE_PRISONER("수감자");

    private final String roleName;

    Role(String roleName) {this.roleName = roleName;}

    public String toRoleName() {return this.roleName;}
}

