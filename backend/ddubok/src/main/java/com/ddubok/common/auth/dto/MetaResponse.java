package com.ddubok.common.auth.dto;

import com.ddubok.api.member.entity.SocialProvider;
import java.util.Map;

public class MetaResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public MetaResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return SocialProvider.META.name();
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

}
