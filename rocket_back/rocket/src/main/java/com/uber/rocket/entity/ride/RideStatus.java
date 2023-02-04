package com.uber.rocket.entity.ride;

public enum RideStatus {
    DENIED, // Driver denied ride
    REQUESTED, //Client waiting for driver to confirm
    CONFIRMED, //Driver confirmed ride for now
    ENDED, //Ride ended
    STARTED, //Ride started
    SCHEDULED, //Driver confirmed ride for future
    //CONFIRMED //Ride is confirmed by all - start simulation
}
