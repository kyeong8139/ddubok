package com.ddubok.api.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialProvider {

    KAKAO("kakao"),
    META("meta"),
    GOOGLE("google"),
    NAVER("naver");

    private final String value;
}