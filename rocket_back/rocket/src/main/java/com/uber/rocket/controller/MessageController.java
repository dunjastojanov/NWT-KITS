package com.uber.rocket.controller;

import com.uber.rocket.dto.CreateMessageDTO;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.service.MessageService;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/chat")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody CreateMessageDTO createMessageDTO, HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(messageService.createMessage(request, createMessageDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAdminChats(HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(messageService.fetchAdminChats(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/message")
    public ResponseEntity<?> getAllMessages(HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(messageService.getMessagesForLoggedUser(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/open/socket/{email}")
    public void getNotification(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/message", messageService.getAllMessageForUser(user));
    }
}
