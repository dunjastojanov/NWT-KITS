package com.uber.rocket.service;

import com.uber.rocket.dto.CreateMessageDTO;
import com.uber.rocket.dto.MessageDTO;
import com.uber.rocket.dto.UserChatInfo;
import com.uber.rocket.entity.user.Message;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    public Object createMessage(HttpServletRequest request, CreateMessageDTO createMessageDTO) {
        User sender = userService.getUserFromRequest(request);
        String receiverEmail = createMessageDTO.getReceiverEmail();
        User receiver = userService.getUserByEmail(receiverEmail);
        String message = createMessageDTO.getMessage();
        Message data = new Message();
        data.setMessage(message);
        data.setSentAt(LocalDateTime.now());
        data.setReceiver(receiver);
        data.setSender(sender);
        messageRepository.save(data);
        return getMessagesBetweenReceiverAndSender(receiver, sender);
    }

    public Object getMessagesBetweenReceiverAndSender(User receiver, User sender) {
        List<Message> messages = messageRepository.findAllByReceiverIsAndSenderIsOrderBySentAtAsc(receiver, sender);
        return messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    public Object getMessagesForLoggedUser(HttpServletRequest request, String receiverEmail) {
        User sender = userService.getUserFromRequest(request);
        User receiver = userService.getUserByEmail(receiverEmail);
        return getMessagesBetweenReceiverAndSender(sender, receiver);
    }


    public Object fetchAdminChats(HttpServletRequest request) {
        User admin = userService.getUserFromRequest(request);
        return messageRepository.findAllDistinctSendersByReceiver(admin).stream().map(UserChatInfo::new).collect(Collectors.toList());
    }

}
