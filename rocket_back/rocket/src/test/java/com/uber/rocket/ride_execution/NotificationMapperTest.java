package com.uber.rocket.ride_execution;

import com.uber.rocket.dto.NotificationDTO;
import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.NotificationMapper;
import com.uber.rocket.utils.TemplateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class NotificationMapperTest {
    @InjectMocks
    NotificationMapper notificationMapper;
    @Mock
    TemplateProcessor templateProcessor;
    AutoCloseable closeable;
    static Notification notification;
    @BeforeEach
    void setUp() {
        closeable = openMocks(this);

        User user = new User();
        user.setId(1L);

        notification = new Notification();
        notification.setUser(user);
        notification.setSent(LocalDateTime.of(2023, 1, 1, 10, 0));
        notification.setType(NotificationType.USER_BLOCKED);
        notification.setRead(false);
        notification.setTitle("Blocked");
        notification.setTemplateVariables("description,You have been blocked because of inappropriate behavior.;");
        notification.setEntityId(null);
        notification.setId(1L);
    }
    @Test
    void mapToDto() {
        when(templateProcessor.process(notification)).thenReturn("<h1>This is an html template</h1>");
        NotificationDTO dto = notificationMapper.mapToDto(notification);

        assertEquals(1L, dto.getId());
        assertFalse(dto.isRead());
        assertEquals("Blocked", dto.getTitle());
        assertEquals(NotificationType.USER_BLOCKED, dto.getType());
        assertNull(dto.getEntityId());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), dto.getSent());
        assertEquals("<h1>This is an html template</h1>", dto.getHtml());
        assertEquals(1L, dto.getUserId());
    }
    @Test
    void mapToDtoNoUser() {
        when(templateProcessor.process(notification)).thenReturn("<h1>This is an html template</h1>");
        notification.setUser(null);
        NotificationDTO dto = notificationMapper.mapToDto(notification);

        assertNull(dto.getUserId());
    }
    @Test
    void mapToDtoNotificationNull() {
        when(templateProcessor.process(notification)).thenReturn("<h1>This is an html template</h1>");
        notification = null;
        assertThrows(NullPointerException.class, () -> {
            notificationMapper.mapToDto(notification);
        });
    }
    @Test
    void mapToDtoNotificationEmpty() {
        when(templateProcessor.process(notification)).thenReturn("<h1>This is an html template</h1>");
        notification = new Notification();
        NotificationDTO dto = notificationMapper.mapToDto(notification);
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getType());
        assertNull(dto.getEntityId());
        assertNull(dto.getSent());
        assertNull(dto.getUserId());
    }
}