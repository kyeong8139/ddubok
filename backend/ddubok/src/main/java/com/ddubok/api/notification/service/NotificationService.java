package com.ddubok.api.notification.service;

public interface NotificationService {

    /**
     * 멤버의 FCM 토큰을 저장한다.
     * 멤버의 알림 수신 여부를 동의로 바꾼다.
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

    /**
     * 카드 오픈 시간이 되었다는 알림을 FCM으로 전송한다.
     *
     * @param memberId 멤버 ID
     * @param title    알림 제목
     * @param body     알림 내용
     */
    void sendCardOpenedPushNotification(String memberId, String title, String body);

    /**
     * 카드가 작성되었다는 알림을 FCM으로 전송한다.
     *
     * @param memberId 멤버 ID
     * @param title    알림 제목
     * @param body     알림 내용
     */
    void sendCardWrittenPushNotification(String memberId, String title, String body);

    /**
     * 출석 체크 알림을 FCM으로 전송한다.
     *
     * @param memberId 멤버 ID
     * @param title    알림 제목
     * @param body     알림 내용
     */
    void sendCheckAttendancePushNotification(String memberId, String title, String body);
}
