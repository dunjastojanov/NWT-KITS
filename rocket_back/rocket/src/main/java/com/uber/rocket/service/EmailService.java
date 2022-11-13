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

    //TODO treba namestiti bolji html template
    private static final String BLOCKED_NOTIFICATION_EMAIL_PATH = "src/main/resources/templates/blocked_notification.html";
    private final static String REGISTRATION_EMAIL_PATH = "src/main/resources/templates/registration_verification.html";
    private final static String PASSWORD_EMAIL_PATH = "src/main/resources/templates/forgotten_password.html";
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
        EmailDataDTO emailDataDTO = new EmailDataDTO();
        emailDataDTO.setRecipient(user.getEmail());
        String content = getEmailFileBySubject(emailSubject);
        content = injectTokenInEmail(token, emailSubject, content);
        emailDataDTO.setContent(content);
        emailDataDTO.setSubject(emailSubject.getLabel());
        sendEmail(emailDataDTO);
    }

    private static String getEmailFileBySubject(EmailSubject emailSubject) {
        try {
            switch (emailSubject) {
                case REGISTRATION_EMAIL -> {
                    return Files.readString(Path.of(REGISTRATION_EMAIL_PATH));
                }
                case FORGOTTEN_PASSWORD -> {
                    return Files.readString(Path.of(PASSWORD_EMAIL_PATH));
                }
                case BLOCKED_NOTIFICATION -> {
                    return Files.readString(Path.of(BLOCKED_NOTIFICATION_EMAIL_PATH));
                }
                default -> {
                    return "";
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(emailSubject.getLabel() + " haven't been loaded");
        }
    }

    private static String injectTokenInEmail(String token, EmailSubject emailSubject, String content) {
        switch (emailSubject) {
            case REGISTRATION_EMAIL -> {
                return content.replace("%s", "https://localhost:8443/api/user/confirm/" + token);
            }
            case FORGOTTEN_PASSWORD -> {
                //TODO treba namestiti stranicu za menjanje lozinke i putanje
                return content.replace("%s", "http://localhost:4200/reset/password/" + token);
            }
            default -> {
                return "";
            }
        }
    }

    public void sendEmailByEmailSubject(User user, EmailSubject emailSubject) {
        EmailDataDTO emailDataDTO = new EmailDataDTO();
        emailDataDTO.setRecipient(user.getEmail());
        String content = getEmailFileBySubject(emailSubject);
        emailDataDTO.setContent(content);
        emailDataDTO.setSubject(emailSubject.getLabel());
        sendEmail(emailDataDTO);
    }
}
