package com.uber.rocket.dto;

import com.uber.rocket.entity.user.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO {
    private String receiver;
    private String sender;
    private String sentAt;
    private String message;

    public MessageDTO(Message message) {
        this.receiver = message.getReceiver().getEmail();
        this.message = message.getMessage();
        this.sender = message.getSender().getEmail();
        this.sentAt= message.getSentAt().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
    }
}
