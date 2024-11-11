package com.ddubok.api.notification.dto.request;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {

    private String userId;
    private String title;
    private String body;
    private Map<String, String> data;
    private LocalDateTime timestamp;

}

