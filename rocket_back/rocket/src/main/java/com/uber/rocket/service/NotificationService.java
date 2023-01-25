package com.uber.rocket.service;

import com.uber.rocket.dto.NotificationDTO;
import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideCancellation;
import com.uber.rocket.entity.user.UpdateDriverDataRequest;
import com.uber.rocket.entity.user.UpdateDriverPictureRequest;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.NotificationMapper;
import com.uber.rocket.repository.NotificationRepository;
import com.uber.rocket.utils.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    TemplateProcessor templateProcessor;

    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserService userService;

    @Autowired
    DestinationService destinationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<NotificationDTO> getNotificationsForUser(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        return getNotificationsForUser(user);
    }

    public List<NotificationDTO> getNotificationsForUser(User user) {
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMINISTRATOR"))) {
            return notificationRepository.findAllByUser(null).stream().map(notificationMapper::mapToDto).toList();
        }
        return notificationRepository.findAllByUser(user).stream().map(notificationMapper::mapToDto).toList();
    }

    public void addUpdateDriverDataRequestNotification(UpdateDriverDataRequest request) {
        Notification notification = new Notification();
        notification.setTitle("Update request");
        notification.setTemplateVariables(templateProcessor.getVariableString(getUpdateDriverDataRequestVariables(request)));
        notification.setEntityId(request.getId());
        notification.setType(NotificationType.UPDATE_DRIVER_DATA_REQUEST);
        save(notification);
    }

    private Notification save(Notification notification) {
        notification.setRead(false);
        notification.setSent(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        System.out.println(notification.getUser().getEmail());
        //messagingTemplate.convertAndSendToUser(notification.getUser().getEmail(), "/queue/notifications", notification.getType());
        return notification;
    }

    public void addUpdateDriverPictureRequestNotification(UpdateDriverPictureRequest request) {
        Notification notification = new Notification();
        notification.setTitle("Update request");
        notification.setTemplateVariables(templateProcessor.getVariableString(getUpdateDriverPictureRequestVariables(request)));
        notification.setEntityId(request.getId());
        notification.setType(NotificationType.UPDATE_DRIVE_PICTURE_REQUEST);
        save(notification);
    }

    private Map<String, String> getUpdateDriverPictureRequestVariables(UpdateDriverPictureRequest request) {
        User driver = userService.getById(request.getDriverId());
        Map<String, String> variables = new HashMap<>();
        variables.put("driver", driver.getFullName());
        variables.put("email", driver.getEmail());
        variables.put("path", request.getProfilePicture());
        return variables;
    }

    private Map<String, String> getUpdateDriverDataRequestVariables(UpdateDriverDataRequest request) {
        User driver = userService.getById(request.getDriverId());
        Map<String, String> variables = new HashMap<>();
        variables.put("firstName", request.getFirstName());
        variables.put("lastName", request.getLastName());
        variables.put("driver", driver.getFullName());
        variables.put("email", driver.getEmail());
        variables.put("phoneNumber", request.getPhoneNumber());
        variables.put("city", request.getCity());
        variables.put("path", getProfilePicture(driver));

        return variables;
    }

    private static String getProfilePicture(User user) {
        String profilePicture = "assets/profile-placeholder.png";
        if (user.getProfilePicture() != null) {
            profilePicture =  user.getProfilePicture();
        }
        return profilePicture;
    }

    public Notification addDriverRideRequestNotification(User user, Ride ride) {
        System.out.println("Ovo se pozove");
        Notification notification = createRideRequestNotification(user, ride);
        notification.setType(NotificationType.DRIVER_RIDE_REQUEST);
        return save(notification);
    }

    public Notification addPassengerRequestNotification(User user, Ride ride) {
        Notification notification = createRideRequestNotification(user, ride);
        notification.setType(NotificationType.PASSENGER_RIDE_REQUEST);
        return save(notification);
    }

    public void addRideCanceledNotifications(RideCancellation rideCancellation) {
        for (Passenger passenger: rideCancellation.getRide().getPassengers()) {
            Notification notification = new Notification();
            notification.setUser(passenger.getUser());
            notification.setType(NotificationType.RIDE_CANCELED);
            notification.setTitle("Ride has been canceled");
            notification.setTemplateVariables(templateProcessor.getVariableString(getRideCancellationVariables(rideCancellation)));
            save(notification);
        }
    }

    public void addRideStatusChangeNotification(Ride ride, User user) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setEntityId(ride.getId());

        switch (ride.getStatus()) {
            case CONFIRMED -> {
                String startAddress = this.destinationService.getStartAddressByRide(ride);
                String endAddress = this.destinationService.getEndAddressByRide(ride);
                notification.setTitle("Driver assigned");
                notification.setType(NotificationType.RIDE_CONFIRMED);
                Map<String, String> variables = getRideVariables(ride, startAddress, endAddress);
                variables.put("status", "Driver has been assigned. They are on their way.");
                notification.setTemplateVariables(templateProcessor.getVariableString(variables));
            }
            case SCHEDULED -> {
                notification.setTitle("Ride scheduled");
                notification.setType(NotificationType.RIDE_CANCELED);
                Map<String, String> variables = getScheduledRideVariables(ride);
                variables.put("status", "Your drive has been scheduled.");
                notification.setTemplateVariables(templateProcessor.getVariableString(variables));
            }
        }

        save(notification);
    }




    private Map<String, String> getScheduledRideVariables(Ride ride) {
        Map<String, String> variables = new HashMap<>();
        String startAddress = this.destinationService.getStartAddressByRide(ride);
        String endAddress = this.destinationService.getEndAddressByRide(ride);
        variables.put("numberOfPassengers", String.valueOf(ride.getPassengers().size()));
        variables.put("price", String.valueOf(ride.getPrice()));
        variables.put("time", String.valueOf(ride.getPrice()));
        variables.put("start", startAddress);
        variables.put("end", endAddress);
        return variables;
    }

    private Map<String, String> getRideCancellationVariables(RideCancellation rideCancellation) {
        Map<String, String> variables = new HashMap<>();
        variables.put("description", rideCancellation.getDescription());
        variables.put("path", getProfilePicture(rideCancellation.getDriver()));
        variables.put("driver", rideCancellation.getDriver().getFullName());
        variables.put("email", rideCancellation.getDriver().getEmail());
        return variables;
    }

    private Notification createRideRequestNotification(User user, Ride ride) {
        String startAddress = this.destinationService.getStartAddressByRide(ride);
        String endAddress = this.destinationService.getEndAddressByRide(ride);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Ride request");
        notification.setTemplateVariables(templateProcessor.getVariableString(getRideVariables(ride, startAddress, endAddress)));
        notification.setEntityId(ride.getId());
        return notification;
    }

    private static Map<String, String> getRideVariables(Ride ride, String startAddress, String endAddress) {
        Map<String, String> variables = new HashMap<>();
        variables.put("numberOfPassengers", String.valueOf(ride.getPassengers().size()));
        //variables.put("path", getProfilePicture(ride.getDriver()));
        //variables.put("driver", ride.getDriver().getFullName());
        //variables.put("email", ride.getDriver().getEmail());
        variables.put("price", String.valueOf(ride.getPrice()));
        variables.put("time", String.valueOf(ride.getPrice()));
        variables.put("start", startAddress);
        variables.put("end", endAddress);
        return variables;
    }


    public Notification setNotificationAsRead(Long id) {
        Optional<Notification> optional = notificationRepository.findById(id);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            notification.setRead(true);
            return notificationRepository.save(notification);
        }
        return null;
    }
}
