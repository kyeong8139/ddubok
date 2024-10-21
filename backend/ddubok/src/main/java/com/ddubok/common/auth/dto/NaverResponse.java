package com.ddubok.common.auth.dto;

public class NaverResponse implements OAuth2Response {

    @Override
    public String getProvider() {
        return "";
    }

    @Override
    public String getProviderId() {
        return "";
    }
}
