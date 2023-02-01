package com.uber.rocket.scheduled;

import com.uber.rocket.service.RideService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
        rideService.updateStatusOverSocket(rideService.getRide(rideId), rideId);
    }
}
