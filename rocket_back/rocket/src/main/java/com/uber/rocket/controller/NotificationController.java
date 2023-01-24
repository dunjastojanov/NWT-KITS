package com.uber.rocket.controller;

import com.uber.rocket.repository.UserRepository;
import com.uber.rocket.service.NotificationService;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<?> getNotificationsForUser(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationsForUser(userService.getUserFromRequest(request)));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> setNotificationAsRead(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(notificationService.setNotificationAsRead(id));
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