package com.ddubok.common.auth.dto;

public interface OAuth2Response {
    String getProvider();

    // 서비스에서 제공하는 사용자 아이디
    String getProviderId();
}
