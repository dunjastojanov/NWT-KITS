package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.repository.NotificationRepository;
import com.uber.rocket.ride_booking.utils.ride.RideCreationService;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.NotificationService;
import com.uber.rocket.utils.TemplateProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static com.uber.rocket.ride_booking.utils.destination.DestinationCreation.getDestination;
import static com.uber.rocket.ride_booking.utils.user.UserCreationService.getGoodClient;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepositoryMock;

    @Mock
    private DestinationService destinationServiceMock;

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private TemplateProcessor templateProcessorMock;

    private AutoCloseable closeable;

    private RideCreationService rideCreationService;

    @BeforeEach
    void initMocks() throws Exception {
        closeable = openMocks(this);
        rideCreationService = new RideCreationService();
    }


    @AfterEach
    void afterMethod() throws Exception {
        closeable.close();
    }


    @Test
    @DisplayName("Sending passenger no start destinations")
    void addPassengerRequestNotificationTest1() {
        Ride ride = rideCreationService.getRideNoPassengers();
        when(this.destinationServiceMock.getStartAddressByRide(ride)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> notificationService.addPassengerRequestNotification(getGoodClient(), ride));
    }

    @Test
    @DisplayName("Sending passenger no end destinations")
    void addPassengerRequestNotificationTest2() {
        Ride ride = rideCreationService.getRideNoPassengers();
        when(this.destinationServiceMock.getStartAddressByRide(ride)).thenReturn(getDestination().getAddress());
        when(this.destinationServiceMock.getEndAddressByRide(ride)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> notificationService.addPassengerRequestNotification(getGoodClient(), ride));
    }


    @Test
    @DisplayName("Sending passenger positive test")
    void addPassengerRequestNotificationTest3() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        when(this.destinationServiceMock.getStartAddressByRide(ride)).thenReturn(getDestination().getAddress());
        when(this.destinationServiceMock.getEndAddressByRide(ride)).thenReturn(getDestination().getAddress());
        when(this.templateProcessorMock.getVariableString(any())).thenReturn("Content");
        notificationService.addPassengerRequestNotification(getGoodClient(), ride);
        verify(notificationRepositoryMock, times(1)).save(any(Notification.class));
    }


    @Test
    @DisplayName("No notification with passed id")
    void setNotificationAsReadTest1() {
        when(this.notificationRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(notificationService.setNotificationAsRead(anyLong()));
        verify(notificationRepositoryMock, never()).save(any(Notification.class));
    }

    @Test
    @DisplayName("Positive test")
    void setNotificationAsReadTest2() {
        when(this.notificationRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Notification()));
        assertNull(notificationService.setNotificationAsRead(anyLong()));
        verify(notificationRepositoryMock, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Sending driver no start destinations")
    void addDriverRideRequestNotificationTest1() {
        Ride ride = rideCreationService.getRideNoPassengers();
        when(this.destinationServiceMock.getStartAddressByRide(ride)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> notificationService.addPassengerRequestNotification(getGoodClient(), ride));
    }

    @Test
    @DisplayName("Sending driver no end destinations")
    void addDriverRideRequestNotificationTest2() {
        Ride ride = rideCreationService.getRideNoPassengers();
        when(this.destinationServiceMock.getStartAddressByRide(ride)).thenReturn(getDestination().getAddress());
        when(this.destinationServiceMock.getEndAddressByRide(ride)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> notificationService.addPassengerRequestNotification(getGoodClient(), ride));
    }


    @Test
    @DisplayName("Sending driver positive test")
    void addDriverRideRequestNotificationTest3() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        when(this.destinationServiceMock.getStartAddressByRide(ride)).thenReturn(getDestination().getAddress());
        when(this.destinationServiceMock.getEndAddressByRide(ride)).thenReturn(getDestination().getAddress());
        when(this.templateProcessorMock.getVariableString(any())).thenReturn("Content");
        notificationService.addPassengerRequestNotification(getGoodClient(), ride);
        verify(notificationRepositoryMock, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Getting notification for user but there is none")
    void getNotificationsForUserTest1() {
        when(notificationRepositoryMock.findByUserAndEntityId(getGoodClient(), 1L)).thenReturn(new ArrayList<>());
        assertThrows(RuntimeException.class, () -> notificationService.getNotificationsForUserAndRide(getGoodClient(), rideCreationService.getRideNoPassengers()));
    }

    @Test
    @DisplayName("Getting notification for user ")
    void getNotificationsForUserTest2() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());
        notifications.add(new Notification());
        when(notificationRepositoryMock.findByUserAndEntityId(getGoodClient(), 1L)).thenReturn(notifications);
        assertEquals(notifications, notificationService.getNotificationsForUserAndRide(getGoodClient(), rideCreationService.getRideNoPassengers()));
    }
}
