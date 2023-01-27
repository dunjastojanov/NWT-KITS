package com.uber.rocket.ride_cancelation.unit;

import com.uber.rocket.dto.NotificationDTO;
import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideCancellation;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.user.Role;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.mapper.NotificationMapper;
import com.uber.rocket.repository.NotificationRepository;
import com.uber.rocket.service.NotificationService;
import com.uber.rocket.utils.TemplateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class NotificationServiceTest {
    @InjectMocks
    NotificationService notificationService;
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    NotificationMapper notificationMapper;
    @Mock
    TemplateProcessor templateProcessor;
    AutoCloseable closeable;
    private RideCancellation cancellation;
    private Passenger passenger;
    private Ride ride;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);

        user1 = new User();
        user1.setFirstName("First name");
        user1.setLastName("Last name");
        user1.setEmail("email@email.com");
        user1.setProfilePicture(null);
        user1.setRoles(List.of(new Role(1L, "CLIENT")));
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);
        user2.setRoles(List.of(new Role(2L, "DRIVER")));

        user3 = new User();
        user3.setRoles(List.of(new Role(3L, "ADMINISTRATOR")));
        user3.setId(3L);

        passenger = new Passenger();
        passenger.setUser(user2);

        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(user1);

        ride = new Ride();
        ride.setVehicle(vehicle);
        ride.setStatus(RideStatus.CONFIRMED);
        ride.setPassengers(List.of(passenger));

        cancellation = new RideCancellation(1L, user1, "I have some health issues", ride);
        when(templateProcessor.process(any())).thenReturn("<h1>This is an example of a template</h1>");


    }

    @Test
    void addRideCanceledNotifications() {
        when(templateProcessor.getVariableString(anyMap())).thenReturn("Test1,This is test 1.;Test2,This is test 2.;");
        List<Notification> notifications = notificationService.addRideCanceledNotifications(cancellation);
        assertEquals(1, notifications.size());

        Notification notification = notifications.get(0);
        assertEquals(NotificationType.RIDE_CANCELED, notification.getType());
        assertEquals(passenger.getUser(), notification.getUser());
        assertEquals(notification.getEntityId(), ride.getId());
        assertEquals("Ride has been canceled", notification.getTitle());

    }

    @Test
    void addRideCanceledNotificationsCancellationIsNull() {
        when(templateProcessor.getVariableString(anyMap())).thenReturn("Test1,This is test 1.;Test2,This is test 2.;");
        assertThrows(NullPointerException.class, () -> {
            notificationService.addRideCanceledNotifications(null);
        });
    }

    @Test
    void addRideCanceledNotificationsCancellationIsEmpty() {
        when(templateProcessor.getVariableString(anyMap())).thenReturn("Test1,This is test 1.;Test2,This is test 2.;");
        assertThrows(NullPointerException.class, () -> {
            notificationService.addRideCanceledNotifications(new RideCancellation());
        });
    }

    @Test
    void addRideCanceledNotificationsPassengersIsEmpty() {
        when(templateProcessor.getVariableString(anyMap())).thenReturn("Test1,This is test 1.;Test2,This is test 2.;");
        ride.setPassengers(new ArrayList<>());
        List<Notification> notifications = notificationService.addRideCanceledNotifications(cancellation);
        assertEquals(0, notifications.size());
    }

    @Test
    void addRideCanceledNotificationsUserIsNull() {
        when(templateProcessor.getVariableString(anyMap())).thenReturn("Test1,This is test 1.;Test2,This is test 2.;");
        ride.setPassengers(List.of(new Passenger()));
        assertThrows(NullPointerException.class, () -> {
            notificationService.addRideCanceledNotifications(new RideCancellation());
        });
    }

    @Test
    public void getNotificationsForAdmin() {
        when(templateProcessor.process(any())).thenReturn("<h1>This is an example of a template</h1>");
        when(notificationRepository.findByUserIsNull()).thenReturn(List.of(new Notification()));
        when(notificationMapper.mapToDto(new Notification())).thenReturn(new NotificationDTO());
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(user3);
        assertEquals(1, notifications.size());
    }

    @Test
    public void getNotificationsForClient() {
        when(notificationMapper.mapToDto(new Notification())).thenReturn(new NotificationDTO());
        when(notificationRepository.findAllByUser(user1)).thenReturn(List.of(new Notification()));
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(user1);
        assertEquals(1, notifications.size());
    }

    @Test
    public void getNotificationsForDriver() {
        when(notificationMapper.mapToDto(new Notification())).thenReturn(new NotificationDTO());
        when(notificationRepository.findAllByUser(user2)).thenReturn(List.of(new Notification()));
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(user2);
        assertEquals(1, notifications.size());
    }

    @Test
    public void getNoNotificationsForClient() {
        when(notificationMapper.mapToDto(new Notification())).thenReturn(new NotificationDTO());
        when(notificationRepository.findAllByUser(user1)).thenReturn(new ArrayList<>());
        List<NotificationDTO> result = notificationService.getNotificationsForUser(user1);
        assertTrue(result.isEmpty());
        verify(notificationRepository).findAllByUser(user1);
        verify(notificationMapper, never()).mapToDto(any());
    }

    @Test
    public void getNoNotificationsForDriver() {
        when(notificationRepository.findAllByUser(user2)).thenReturn(new ArrayList<>());
        List<NotificationDTO> result = notificationService.getNotificationsForUser(user2);
        assertTrue(result.isEmpty());
        verify(notificationRepository).findAllByUser(user2);
        verify(notificationMapper, never()).mapToDto(any());
    }

    @Test
    public void getNoNotificationsForAdmin() {
        when(notificationRepository.findByUserIsNull()).thenReturn(Collections.emptyList());
        List<NotificationDTO> result = notificationService.getNotificationsForUser(user3);
        assertTrue(result.isEmpty());
        verify(notificationRepository).findByUserIsNull();
        verify(notificationMapper, never()).mapToDto(any());
    }

    @Test
    public void setNotificationAsRead() {
        Long id = 1L;
        Notification notification = new Notification();
        notification.setId(id);
        notification.setRead(false);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.setNotificationAsRead(id);

        assertEquals(notification, result);
        assertTrue(result.isRead());
        verify(notificationRepository).findById(id);
        verify(notificationRepository).save(notification);
    }

    @Test
    public void setNotificationAsReadInvalidId() {
        Long id = 1L;
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        Notification result = notificationService.setNotificationAsRead(id);

        assertNull(result);
        verify(notificationRepository).findById(id);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    public void setNotificationAsReadIdNull() {
        when(notificationRepository.findById(null)).thenThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> {
            notificationService.setNotificationAsRead(null);
        });
    }

}