package com.uber.rocket.controller;

import com.uber.rocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import com.uber.rocket.service.NotificationService;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public ResponseEntity<?> getNotificationsForUser(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationsForUser(request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @GetMapping("/greet")
    public String getNotification() {
        System.out.println("Greet se pozove");
        userRepository.findAll().forEach(user -> {
            System.out.println("User: " + user.getEmail());
            messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", "Hello, " + user.getEmail() + "!");
        });

        return "Notification here yay";
    }
}