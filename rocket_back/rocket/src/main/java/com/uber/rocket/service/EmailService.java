package com.uber.rocket.service;

import com.uber.rocket.dto.EmailDataDTO;
import com.uber.rocket.dto.EmailSubject;
import com.uber.rocket.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String sender;

    @Async
    public void sendEmail(EmailDataDTO emailDataDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailDataDTO.getContent(), true);
            helper.setTo(emailDataDTO.getRecipient());
            helper.setSubject(emailDataDTO.getSubject());
            helper.setFrom(sender);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }

    public void sendRegistrationEmail(User user, String token) {
        //TODO napraviti template za emailove
        EmailDataDTO emailDataDTO = new EmailDataDTO();
        emailDataDTO.setRecipient(user.getEmail());
        emailDataDTO.setContent("<html><head></head><body><a href=https://localhost:8443/api/user/confirm/" + token + ">Aktivirajte</a></body></html>");
        emailDataDTO.setSubject(EmailSubject.REGISTRATION_EMAIL.label);
        sendEmail(emailDataDTO);
    }
}
