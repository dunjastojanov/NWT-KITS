package com.uber.rocket.controller;

import com.uber.rocket.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
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








}
