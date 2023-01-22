package com.uber.rocket.entity.ride;

public enum UserRidingStatus {

    WAITING("Waiting..."),
    ACCEPTED("Accepted"),
    DENIED("Denied");
    private final String name;
    UserRidingStatus(String name) {
        this.name = name;
    }
}
