package com.uber.rocket.scheduled;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class SendReminderTask implements Runnable {

    private SimpMessagingTemplate messagingTemplate;
    private NotificationService notificationService;
    private Ride ride;

    public SendReminderTask(SimpMessagingTemplate messagingTemplate, NotificationService notificationService, Ride ride) {
        this.ride = ride;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        List<User> users = ride.getUsers();
        users.add(ride.getDriver());
        users.forEach(user -> {
            notificationService.addRideStatusChangeNotification(ride, user);
            messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", notificationService.getNotificationsForUser(user));
        });
    }
}
