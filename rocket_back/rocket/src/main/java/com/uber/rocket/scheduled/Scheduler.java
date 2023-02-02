package com.uber.rocket.scheduled;

import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.repository.RideCancellationRepository;
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

    private final static int ONE_MINUTE = 1000 * 60;
    @Autowired
    private RideCancellationRepository rideCancellationRepository;

    @Scheduled(fixedDelay = ONE_MINUTE, initialDelay = ONE_MINUTE * 10)
    public void checkIfDriveExceededWorkingTime() {
        try {
            List<Vehicle> vehicles = vehicleService.getAllActiveVehicles(VehicleStatus.ACTIVE);
            if (vehicles.isEmpty()) {
                return;
            }
            for (Vehicle vehicle : vehicles) {
                boolean exceeded = logInfoService.hasDriverExceededWorkingHours(vehicle.getDriver().getId());
                RideDTO rideDTO=rideService.getUserCurrentRideByEmail(vehicle.getDriver().getEmail());
                if (exceeded && rideDTO==null) {
                    vehicle.setStatus(VehicleStatus.INACTIVE);
                    vehicleService.save(vehicle);
                    logInfoService.endWorkingHourCount(vehicle.getDriver().getId());
                    messagingTemplate.convertAndSendToUser(vehicle.getDriver().getEmail(), "/queue/driver-status", vehicle.getStatus());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = ONE_MINUTE, initialDelay = ONE_MINUTE)
    public void sendRemindersForFutureRide() {
        try {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
            List<Ride> rides = rideService.getRidesByRideStatus(RideStatus.SCHEDULED);
            if (rides.isEmpty()) {
                return;
            }
            for (Ride ride : rides) {
                if (rideService.checkIfDriverHasConfirmedOrStartedRide(ride.getDriver().getId())) {
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
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
