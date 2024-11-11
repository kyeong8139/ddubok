package com.ddubok.api.notification.service;

import java.util.List;

public interface NotificationService {

    /**
     * 멤버의 FCM 토큰을 저장한다. 멤버의 알림 수신 여부를 동의로 바꾼다.
     *
     * @param memberId 멤버 ID
     * @param token    해당 멤버의 FCM 토큰
     */
    void saveToken(Long memberId, String token);

    /**
     * 멤버의 FCM 토큰을 삭제한다.
     *
     * @param memberId 멤버 ID
     */
    void deleteToken(Long memberId);

}
