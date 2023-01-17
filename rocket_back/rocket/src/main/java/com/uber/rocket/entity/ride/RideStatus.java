package com.uber.rocket.entity.ride;

public enum RideStatus {
    REQUESTED, //Client waiting for driver to confirm
    ENDED, //Ride ended
    STARTED, //Ride started
    SCHEDULED, //Ride is scheduled for future
    CONFIRMED //Ride is confirmed by driver
}
