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

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String sender;

    private final static String REPLACEMENT_STRING = "%s";
    private final static String APP_LOGO_PATH = "src/main/resources/static/images/email/logo.png";
    private final static String FACEBOOK_LOGO_PATH = "src/main/resources/static/images/email/facebook.png";
    private final static String INSTAGRAM_LOGO_PATH = "src/main/resources/static/images/email/instagram.png";
    private final static String LINKEDIN_LOGO_PATH = "src/main/resources/static/images/email/linkedin.png";
    private final static String MAIL_LOGO_PATH = "src/main/resources/static/images/email/mail.png";
    private final static String PHONE_LOGO_PATH = "src/main/resources/static/images/email/phone.png";

    @Async
    public void sendEmail(EmailDataDTO emailDataDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setText(emailDataDTO.getContent(), true);
            helper.setTo(emailDataDTO.getRecipient());
            helper.setSubject(emailDataDTO.getSubject());
            helper.setFrom(sender);
            DataSource app_logo = new FileDataSource(APP_LOGO_PATH);
            DataSource facebook_logo = new FileDataSource(FACEBOOK_LOGO_PATH);
            DataSource instagram_logo = new FileDataSource(INSTAGRAM_LOGO_PATH);
            DataSource linkedin_logo = new FileDataSource(LINKEDIN_LOGO_PATH);
            DataSource mail_logo = new FileDataSource(MAIL_LOGO_PATH);
            DataSource phone_logo = new FileDataSource(PHONE_LOGO_PATH);
            helper.addInline("app_logo", app_logo);
            helper.addInline("facebook_logo", facebook_logo);
            helper.addInline("instagram_logo", instagram_logo);
            helper.addInline("linkedin_logo", linkedin_logo);
            helper.addInline("mail_logo", mail_logo);
            helper.addInline("phone_logo", phone_logo);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }


    public void sendEmailWithTokenByEmailSubject(User user, String token, EmailSubject emailSubject) throws IOException {
        EmailDataDTO emailDataDTO = buildEmailDTO(user, emailSubject);
        String content = loadEmailTemplate(emailSubject);
        content = modifyEmailContent(token, emailSubject, content);
        emailDataDTO.setContent(content);
        sendEmail(emailDataDTO);
    }

    public void sendEmailByEmailSubject(User user, EmailSubject emailSubject) {
        EmailDataDTO emailDataDTO = buildEmailDTO(user, emailSubject);
        String content = loadEmailTemplate(emailSubject);
        emailDataDTO.setContent(content);
        sendEmail(emailDataDTO);
    }


    private static String loadEmailTemplate(EmailSubject emailSubject) {
        try {
            return Files.readString(Path.of(emailSubject.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(emailSubject.getLabel() + " haven't been loaded");
        }
    }

    private static String modifyEmailContent(String injectedString, EmailSubject emailSubject, String content) {
        switch (emailSubject) {
            case REGISTRATION_EMAIL -> {
                return content.replace(REPLACEMENT_STRING, "http://localhost:4200/registration/verification?token=" + injectedString);
            }
            case FORGOTTEN_PASSWORD -> {
                return content.replace(REPLACEMENT_STRING, "http://localhost:4200?token=" + injectedString);
            }
            case DRIVER_REGISTRATION_NOTIFICATION -> {
                return content.replace(REPLACEMENT_STRING, injectedString);
            }
            default -> {
                return content;
            }
        }
    }

    private static EmailDataDTO buildEmailDTO(User user, EmailSubject emailSubject) {
        EmailDataDTO emailDataDTO = new EmailDataDTO();
        emailDataDTO.setRecipient(user.getEmail());
        emailDataDTO.setSubject(emailSubject.getLabel());
        return emailDataDTO;
    }

}
