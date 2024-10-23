package com.ddubok.common.auth.dto;

import com.ddubok.api.member.entity.SocialProvider;
import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public GoogleResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return SocialProvider.GOOGLE.name();
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }
}
