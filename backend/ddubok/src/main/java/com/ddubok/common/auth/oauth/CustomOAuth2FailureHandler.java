package com.ddubok.common.auth.oauth;

import com.ddubok.common.template.response.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * OAuth2 인증 실패를 처리하는 핸들러 클래스입니다. SimpleUrlAuthenticationFailureHandler를 확장하여 OAuth2 인증 실패 시의 동작을
 * 정의합니다.
 */
@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * OAuth2 인증 실패 시 호출되는 메서드 Ajax 요청인 경우 JSON 응답을, 일반 요청인 경우 리다이렉트를 처리
     *
     * @param request   HTTP 요청 객체
     * @param response  HTTP 응답 객체
     * @param exception 발생한 인증 예외
     * @throws IOException      I/O 처리 중 발생할 수 있는 예외
     * @throws ServletException 서블릿 처리 중 발생할 수 있는 예외
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "로그인에 실패했습니다";
        if (exception instanceof OAuth2AuthenticationException) {
            errorMessage = "OAuth2 로그인 오류: " + exception.getMessage();
        }

        if (isAjaxRequest(request)) {
            response.setStatus(Integer.parseInt(ResponseCode.LOGIN_FAILED.getCode()));
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(String.format("{\"message\":\"%s\"}", errorMessage));
        } else {
            response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
        }
    }

    /**
     * 요청이 Ajax 요청인지 확인하는 메서드입니다.
     *
     * @param request 검사할 HTTP 요청 객체
     * @return Ajax 요청인 경우 true, 아닌 경우 false
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
            "application/json".equals(request.getHeader("Accept"));
    }

}
