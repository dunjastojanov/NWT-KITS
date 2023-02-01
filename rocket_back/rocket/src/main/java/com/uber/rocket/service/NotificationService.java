package com.uber.rocket.service;

import com.uber.rocket.dto.NotificationDTO;
import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideCancellation;
import com.uber.rocket.entity.user.UpdateDriverDataRequest;
import com.uber.rocket.entity.user.UpdateDriverPictureRequest;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.NotificationMapper;
import com.uber.rocket.repository.NotificationRepository;
import com.uber.rocket.repository.UserRepository;
import com.uber.rocket.utils.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    UserRepository userRepository;

    @Autowired
    AuthService authService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    DestinationService destinationService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. hh:mm");

    public List<NotificationDTO> getNotificationsForUser(User user) {
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMINISTRATOR"))) {
            List<Notification> byUserIsNull = notificationRepository.findByUserIsNull();
            return byUserIsNull.stream().map(notificationMapper::mapToDto).toList();
        }
        return notificationRepository.findAllByUser(user).stream()
                .map(notificationMapper::mapToDto)
                .filter(notificationDTO -> !notificationDTO.isRead()).toList();
    }

    public void addUpdateDriverDataRequestNotification(UpdateDriverDataRequest request) {
        Notification notification = new Notification();
        notification.setTitle("Update request");
        notification.setTemplateVariables(templateProcessor.getVariableString(getUpdateDriverDataRequestVariables(request)));
        notification.setEntityId(request.getId());
        notification.setType(NotificationType.UPDATE_DRIVER_DATA_REQUEST);
        save(notification);
    }

    public Notification save(Notification notification) {
        notification.setRead(false);
        notification.setSent(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        return notification;
    }

    public void addUpdateDriverPictureRequestNotification(UpdateDriverPictureRequest request) {
        Notification notification = new Notification();
        notification.setTitle("Update request");
        notification.setTemplateVariables(templateProcessor.getVariableString(getUpdateDriverPictureRequestVariables(request)));
        notification.setEntityId(request.getId());
        notification.setType(NotificationType.UPDATE_DRIVER_PICTURE_REQUEST);
        save(notification);
    }

    private Map<String, String> getUpdateDriverPictureRequestVariables(UpdateDriverPictureRequest request) {
        User driver = getDriver(request.getDriverId());
        Map<String, String> variables = new HashMap<>();
        variables.put("driver", driver.getFullName());
        variables.put("email", driver.getEmail());
        variables.put("path", request.getProfilePicture());
        return variables;
    }

    private Map<String, String> getUpdateDriverDataRequestVariables(UpdateDriverDataRequest request) {
        User driver = getDriver(request.getDriverId());
        Map<String, String> variables = new HashMap<>();
        variables.put("firstName", request.getFirstName());
        variables.put("lastName", request.getLastName());
        variables.put("driver", driver.getFullName());
        variables.put("email", driver.getEmail());
        variables.put("phoneNumber", request.getPhoneNumber());
        variables.put("city", request.getCity());
        variables.put("path", getProfilePicture(driver));
        variables.put("features", getAdditionalFeatures(request));
        variables.put("type", request.getType().getName());
        return variables;
    }

    private static String getAdditionalFeatures(UpdateDriverDataRequest request) {
        List<String> features = new ArrayList<>();
        if (request.isKidFriendly()) {
            features.add("Kid friendly");
        }
        if (request.isPetFriendly()) {
            features.add("Pet friendly");
        }
        return String.join(" && ", features);
    }

    private User getDriver(Long request) {
        Optional<User> optional = userRepository.findById(request);
        if (optional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return optional.get();
    }

    private static String getProfilePicture(User user) {
        String profilePicture = "assets/profile-placeholder.png";
        if (user.getProfilePicture() != null) {
            profilePicture = user.getProfilePicture();
        }
        return profilePicture;
    }

    public Notification addDriverRideRequestNotification(User user, Ride ride) {
        Notification notification = createRideRequestNotification(user, ride);
        notification.setType(NotificationType.DRIVER_RIDE_REQUEST);
        return save(notification);
    }

    public void addPassengerRequestNotification(User user, Ride ride) {
        Notification notification = createRideRequestNotification(user, ride);
        notification.setType(NotificationType.PASSENGER_RIDE_REQUEST);
        save(notification);
    }

    public List<Notification> addRideCanceledNotifications(RideCancellation rideCancellation) {
        List<Notification> notifications = new ArrayList<>();
        for (Passenger passenger : rideCancellation.getRide().getPassengers()) {
            Notification notification = new Notification();
            notification.setUser(passenger.getUser());
            notification.setType(NotificationType.RIDE_CANCELED);
            notification.setTitle("Ride has been canceled");
            notification.setSent(LocalDateTime.now());
            notification.setTemplateVariables(templateProcessor.getVariableString(getRideCancellationVariables(rideCancellation)));
            notifications.add(notification);
            save(notification);
        }
        return notifications;
    }

    public void addRideReviewNotification(User user, Ride ride) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(NotificationType.RIDE_REVIEW);
        notification.setTitle("Ride review");
        Map<String, String> variables = getRideVariables(ride, this.destinationService.getStartAddressByRide(ride), this.destinationService.getEndAddressByRide(ride));
        variables.put("status", "Your ride has ended. Leave a review for your driver and for their vehicle.");
        notification.setTemplateVariables(templateProcessor.getVariableString(variables));
        notification.setEntityId(ride.getId());
        save(notification);
    }

    public void addBlockedUserNotification(String reason, User user) {
        Notification notification = createBlockedUserNotification(reason, user);
        notification.setType(NotificationType.USER_BLOCKED);
        save(notification);
    }

    private Notification createBlockedUserNotification(String reason, User user) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Blocked");
        notification.setTemplateVariables(templateProcessor.getVariableString(getBlockedUserVariables(reason)));
        return notification;
    }

    private Map<String, String> getBlockedUserVariables(String reason) {
        Map<String, String> variables = new HashMap<>();
        variables.put("description", reason);
        return variables;
    }

    public void addDriverArrivedNotification(Ride ride, User user) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setEntityId(ride.getId());
        String startAddress = this.destinationService.getStartAddressByRide(ride);
        String endAddress = this.destinationService.getEndAddressByRide(ride);
        notification.setTitle("Vehicle arrived");
        notification.setType(NotificationType.RIDE_CONFIRMED);
        Map<String, String> variables = getRideVariables(ride, startAddress, endAddress);
        variables.put("status", "Vehicle has arrived.");
        notification.setTemplateVariables(templateProcessor.getVariableString(variables));
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
                notification.setTitle("Ride scheduled for: " + ride.getStartTime().format(formatter));
                notification.setType(NotificationType.RIDE_SCHEDULED);
                Map<String, String> variables = getScheduledRideVariables(ride);
                notification.setTemplateVariables(templateProcessor.getVariableString(variables));
            }
        }
        save(notification);
    }

    private Map<String, String> getScheduledRideVariables(Ride ride) {
        Map<String, String> variables = new HashMap<>();
        String startAddress = this.destinationService.getStartAddressByRide(ride);
        String endAddress = this.destinationService.getEndAddressByRide(ride);
        variables.put("passengers", getPassengers(ride));
        variables.put("price", String.valueOf(ride.getPrice()));
        variables.put("time", String.valueOf(ride.getPrice()));
        variables.put("start", startAddress);
        variables.put("end", endAddress);
        return variables;
    }

    private static String getPassengers(Ride ride) {
        List<String> passengers = new ArrayList<>();
        for (User passenger : ride.getUsers()) {
            passengers.add(passenger.getFullName());
        }
        return String.join(" && ", passengers);
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
        HashMap<String, String> variables = (HashMap<String, String>) getRideVariables(ride, startAddress, endAddress);
        notification.setTemplateVariables(templateProcessor.getVariableString(variables));
        notification.setEntityId(ride.getId());
        notification.setSent(LocalDateTime.now());
        return notification;
    }

    private static Map<String, String> getRideVariables(Ride ride, String startAddress, String endAddress) {
        Map<String, String> variables = new HashMap<>();
        variables.put("passengers", getPassengers(ride));
        variables.put("price", String.valueOf(ride.getPrice()));
        variables.put("time", String.valueOf(ride.getPrice()));
        variables.put("start", startAddress);
        variables.put("end", endAddress);

        if (ride.getVehicle() != null) {
            if (ride.getDriver() != null) {
                variables.put("driver", ride.getDriver().getFullName());
                variables.put("email", ride.getDriver().getEmail());
                variables.put("path", getProfilePicture(ride.getDriver()));
            }
        }
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

    public void deleteNotification(Long entityId) {
        Optional<Notification> notification = notificationRepository.findNotificationByEntityId(entityId);
        notification.ifPresent(value -> notificationRepository.delete(value));
    }

    public void deleteNotificationById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        notification.ifPresent(value -> notificationRepository.delete(value));
    }

    public List<Notification> getNotificationsForUserAndRide(User user, Ride ride) {
        List<Notification> notifications = notificationRepository.findByUserAndEntityId(user, ride.getId());
        if (notifications.size() > 0) {
            return notifications;
        } else throw new RuntimeException("There are no notifications for given user and ride.");
    }

    public void setNotificationAsRead(User user, Ride ride, NotificationType type) {
        List<Notification> notifs = getNotificationsForUserAndRide(user, ride);
        for (Notification notification : notifs) {
            if (notification.getType() == type)
                setNotificationAsRead(notification.getId());
        }
        messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", getNotificationsForUser(user));
    }
}
