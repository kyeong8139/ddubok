package com.ddubok.api.notification.dto.request;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {

    private Long id;
    private String title;
    private String body;
    private Map<String, String> data;
    private LocalDateTime timestamp;

}

