package com.uber.rocket.scheduled;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.service.LogInfoService;
import com.uber.rocket.service.NotificationService;
import com.uber.rocket.service.RideService;
import com.uber.rocket.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class Scheduler {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RideService rideService;
    @Autowired
    private LogInfoService logInfoService;


    @Scheduled(fixedDelay = 5000)
    public void checkIfDriveExceededWorkingTime() {
        List<Vehicle> vehicles = vehicleService.getAllActiveVehicles(VehicleStatus.ACTIVE);
        if (vehicles.isEmpty())
            return;
        for (Vehicle vehicle : vehicles) {
            boolean exceeded = logInfoService.hasDriverExceededWorkingHours(vehicle.getDriver().getId());
            if (exceeded) {
                vehicle.setStatus(VehicleStatus.INACTIVE);
                vehicleService.save(vehicle);
                messagingTemplate.convertAndSendToUser(vehicle.getDriver().getEmail(), "/user/queue/driver/status", vehicle.getStatus());
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void sendRemindersForFutureRide() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        List<Ride> rides = rideService.getRidesByRideStatus(RideStatus.SCHEDULED);
        if (rides.isEmpty())
            return;
        for (Ride ride : rides) {
            LocalDateTime scheduledTime = ride.getStartTime().minusMinutes(15);
            long initialDelay = ChronoUnit.MILLIS.between(LocalDateTime.now(), scheduledTime);
            executor.scheduleAtFixedRate(new SendReminderTask(messagingTemplate, notificationService, ride), initialDelay, 1, TimeUnit.MINUTES);

            scheduledTime = ride.getStartTime().plusMinutes(5);
            initialDelay = ChronoUnit.MILLIS.between(LocalDateTime.now(), scheduledTime);
            executor.scheduleAtFixedRate(new SendReminderTask(messagingTemplate, notificationService, ride), initialDelay, 1, TimeUnit.MINUTES);

            scheduledTime = ride.getStartTime().plusMinutes(5);
            initialDelay = ChronoUnit.MILLIS.between(LocalDateTime.now(), scheduledTime);
            executor.scheduleAtFixedRate(new SendReminderTask(messagingTemplate, notificationService, ride), initialDelay, 1, TimeUnit.MINUTES);

            scheduledTime = ride.getStartTime().plusMinutes(5);
            initialDelay = ChronoUnit.MILLIS.between(LocalDateTime.now(), scheduledTime);
            executor.scheduleAtFixedRate(new SetRideStatus(ride.getId(), rideService), initialDelay, 1, TimeUnit.DAYS);
        }
    }

}
