package com.ddubok.api.member.dto.response;

import com.ddubok.api.member.entity.NotificationConsent;
import javax.management.Notification;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailRes {

    private String nickname;
    private NotificationConsent notificationConsent;

}
