package com.uber.rocket.scheduled;

import com.uber.rocket.service.RideService;

public class SetRideStatus implements Runnable {

    private RideService rideService;


    private Long rideId;

    public SetRideStatus(Long rideId, RideService rideService) {
        this.rideId = rideId;
        this.rideService = rideService;
    }

    @Override
    public void run() {
        rideService.changeRideStatusToConfirm(rideId);
    }
}
