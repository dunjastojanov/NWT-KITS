package com.uber.rocket.controller;

import com.uber.rocket.dto.EmailDataDTO;
import com.uber.rocket.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@RestController
@RequestMapping("/api/email/test")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public void sendEmail() {
        try {
            EmailDataDTO emailDataDTO = new EmailDataDTO(
                    "rocket9client@gmail.com",
                    "",
                    "Registration verification"
            );
            String token = UUID.randomUUID().toString();
            String nesto = Files.readString(Path.of("src/main/resources/templates/forgotten_password.html"));
            String href = "https://localhost:8443/api/user/confirm/" + token;
            nesto = nesto.replace("%s", href);
            emailDataDTO.setContent(nesto);
            emailService.sendEmail(emailDataDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
