package com.uber.rocket.dto;

public enum EmailSubject {
    REGISTRATION_EMAIL("Registration email"), FORGOTTEN_PASSWORD("Forgotten password email");

    public final String label;

    EmailSubject(String label) {
        this.label = label;
    }
}
