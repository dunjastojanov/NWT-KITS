package com.uber.rocket.controller;

import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;

import com.uber.rocket.service.NotificationService;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

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
    @PutMapping("/{id}")
    public ResponseEntity<?> setNotificationAsRead(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(notificationService.setNotificationAsRead(id));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/send-ride-request-invitation/{email}")
    public String getNotification(@PathVariable("email") String email) {
        System.out.println("Greet se pozove");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            messagingTemplate.convertAndSendToUser(user.get().getEmail(), "/queue/notifications", notificationService.getNotificationsForUser(user.get()));
        return "Notification here yay";
    }
}