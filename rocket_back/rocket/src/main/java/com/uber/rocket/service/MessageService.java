package com.uber.rocket.service;

import com.uber.rocket.dto.UserChatInfo;
import com.uber.rocket.entity.user.Message;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    public Object createMessage(HttpServletRequest request) {
        User sender = userService.getUserFromRequest(request);
        String receiverEmail = request.getParameter("receiverEmail");
        User receiver = userService.getUserByEmail(receiverEmail);
        String message = request.getParameter("message");
        Message data = new Message();
        data.setMessage(message);
        data.setSentAt(LocalDateTime.now());
        data.setReceiver(receiver);
        data.setSender(sender);
        messageRepository.save(data);
        return getMessagesForLoggedUser(request);
    }

    public Object getMessagesForLoggedUser(HttpServletRequest request) {
        User sender = userService.getUserFromRequest(request);
        String receiverEmail = request.getParameter("receiverEmail");
        User receiver = userService.getUserByEmail(receiverEmail);
        return messageRepository.findAllByReceiverIsAndSenderIsOrderBySentAtAsc(receiver, sender);
    }


    public Object fetchAdminChats(HttpServletRequest request) {
        User admin = userService.getUserFromRequest(request);
        return messageRepository.findDistinctSendersByReceiverIs(admin).stream().map(UserChatInfo::new).collect(Collectors.toList());
    }

}
