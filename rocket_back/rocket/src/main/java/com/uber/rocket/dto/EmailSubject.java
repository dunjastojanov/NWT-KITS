package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailSubject {
    REGISTRATION_EMAIL("src/main/resources/templates/registration_verification.html", "Registration email"),
    FORGOTTEN_PASSWORD("src/main/resources/templates/forgotten_password.html", "Forgotten password email"),
    BLOCKED_NOTIFICATION("src/main/resources/templates/blocked_notification.html", "Blocked notification email"),
    DRIVER_REGISTRATION_NOTIFICATION("src/main/resources/templates/driver_registration.html", "Driver registration email");

    private final String path;
    private final String label;

}
