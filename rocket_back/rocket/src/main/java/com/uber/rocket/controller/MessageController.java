package com.uber.rocket.controller;

import com.uber.rocket.dto.CreateMessageDTO;
import com.uber.rocket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/chat")
public class MessageController {
    @Autowired
    private MessageService messageService;

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

    @GetMapping(path = "/message/{receiverEmail}")
    public ResponseEntity<?> getAllMessages(HttpServletRequest request, @PathVariable String receiverEmail) {
        try {
            return ResponseEntity.ok().body(messageService.getMessagesForLoggedUser(request,receiverEmail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
