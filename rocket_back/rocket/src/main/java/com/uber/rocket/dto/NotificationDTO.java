package com.uber.rocket.dto;

import com.uber.rocket.entity.notification.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String html;
    private String title;
    private NotificationType type;
    private boolean read;
    private Long entityId;
    private LocalDateTime sent;

}
