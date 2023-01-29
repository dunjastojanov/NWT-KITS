package com.uber.rocket.service;

import com.uber.rocket.dto.NewReviewDTO;
import com.uber.rocket.entity.notification.Notification;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    public List<Review> getReviewByPassenger(User passenger) {
        return reviewRepository.findAllByPassenger(passenger);
    }

    public List<Review> getReviewByRide(Ride ride) {
        return reviewRepository.findAllByRide(ride);
    }

    public Review addReview(HttpServletRequest request, NewReviewDTO dto, Ride ride) throws RuntimeException{
        if (ride.getEndTime().isBefore(LocalDateTime.now().minusDays(3))) {
            throw new RuntimeException("Your time window for leaving a review has passed.");
        }
        Review review = new Review();
        User user = userService.getUserFromRequest(request);
        review.setPassenger(user);
        review.setDescription(dto.getDescription());
        review.setDriverRating(dto.getDriverRating());
        review.setVehicleRating(dto.getVehicleRating());
        review.setRide(ride);

        review =  reviewRepository.save(review);
        List<Notification> notifs = notificationService.getNotificationsForUserAndRide(user, ride);
        for (Notification notification : notifs) {
            if (notification.getType() == NotificationType.RIDE_REVIEW) {
                notificationService.setNotificationAsRead(user, ride, notification.getType());
            }
        }
        return review;
    }
}
