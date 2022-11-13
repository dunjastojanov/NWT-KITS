package com.uber.rocket.dto;

import lombok.Getter;

@Getter
public enum EmailSubject {
    REGISTRATION_EMAIL("Registration email"), FORGOTTEN_PASSWORD("Forgotten password email"), BLOCKED_NOTIFICATION("Blocked notification");

    private final String label;

    EmailSubject(String label) {
        this.label = label;
    }
}
