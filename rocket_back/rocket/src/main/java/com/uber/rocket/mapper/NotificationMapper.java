package com.uber.rocket.mapper;

import com.uber.rocket.dto.NotificationDTO;
import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.utils.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper implements Mapper<Notification, NotificationDTO>{

    @Autowired
    TemplateProcessor templateProcessor;
    @Override
    public NotificationDTO mapToDto(Notification notification) {
        NotificationDTO dto = NotificationDTO.builder()
                .title(notification.getTitle())
                .id(notification.getId())
                .read(notification.isRead())
                .entityId(notification.getEntityId())
                .type(notification.getType())
                .sent(notification.getSent())
                .html(templateProcessor.process(notification)).build();

        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getId());
        }
        return dto;
    }
}
