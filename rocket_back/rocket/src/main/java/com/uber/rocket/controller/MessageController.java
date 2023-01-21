package com.uber.rocket.controller;

import com.uber.rocket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/chat")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> createMessage(HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(messageService.createMessage(request));
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
}
