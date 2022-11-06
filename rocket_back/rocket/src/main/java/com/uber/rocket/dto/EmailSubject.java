package com.uber.rocket.dto;

public enum EmailSubject {
    REGISTRATION_EMAIL("Registration email");

    public final String label;

    EmailSubject(String label) {
        this.label = label;
    }
}
