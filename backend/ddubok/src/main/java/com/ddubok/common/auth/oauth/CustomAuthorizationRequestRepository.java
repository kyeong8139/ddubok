package com.ddubok.common.auth.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final String CODE_VERIFIER = "challenge";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String registrationId = extractRegistrationId(request.getRequestURI());

        if ("x".equals(registrationId)) {
            return (OAuth2AuthorizationRequest) request.getSession()
                .getAttribute("oauth2_auth_request");
        }

        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest != null) {
            String registrationId = extractRegistrationId(request.getRequestURI());

            if ("x".equals(registrationId)) {
                request.getSession().setAttribute("oauth2_auth_request", authorizationRequest);
                request.getSession().setAttribute("code_verifier", CODE_VERIFIER);
            }
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
        HttpServletResponse response) {
        String registrationId = extractRegistrationId(request.getRequestURI());

        if ("x".equals(registrationId)) {
            OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
            if (authorizationRequest != null) {
                request.getSession().removeAttribute("oauth2_auth_request");
                request.getSession().removeAttribute("code_verifier");
            }
            return authorizationRequest;
        }

        OAuth2AuthorizationRequest authorizationRequest = (OAuth2AuthorizationRequest) request.getSession().getAttribute("oauth2_auth_request");
        request.getSession().removeAttribute("oauth2_auth_request");
        return authorizationRequest;
    }

    private String extractRegistrationId(String uri) {
        if (uri != null && uri.contains("/oauth2/authorization/")) {
            return uri.substring(uri.lastIndexOf("/") + 1);
        }
        return "";
    }
}