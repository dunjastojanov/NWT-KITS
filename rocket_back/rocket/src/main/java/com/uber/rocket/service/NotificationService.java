package com.uber.rocket.service;

import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.NotificationRepository;
import com.uber.rocket.utils.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    TemplateProcessor templateProcessor;
    @Autowired
    UserService userService;

    public List<Notification> getNotificationsForUser(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        return notificationRepository.findAllByUser(user);
    }
    public Notification addDriverRideRequestNotification(User user, Ride ride) {
        Notification notification = createRideRequestNotification(user, ride);
        notification.setType(NotificationType.DRIVER_RIDE_REQUEST);
        return notificationRepository.save(notification);
    }
    public Notification addPassengerRequestNotification(User user, Ride ride) {
        Notification notification = createRideRequestNotification(user, ride);
        notification.setType(NotificationType.PASSENGER_RIDE_REQUEST);
        return notificationRepository.save(notification);
    }

    private static Notification createRideRequestNotification(User user, Ride ride) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Ride request");
        notification.setHtml("");
        notification.setRead(false);
        notification.setEntityId(ride.getId());
        return notification;
    }


}
