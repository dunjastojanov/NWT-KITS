package com.uber.rocket.service;

import com.uber.rocket.dto.NewReviewDTO;
import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;


    public List<Review> getReviewByPassenger(User passenger) {
        return reviewRepository.findAllByPassenger(passenger);
    }

    public List<Review> getReviewByRide(Ride ride) {
        return reviewRepository.findAllByRide(ride);
    }

    public Review addReview(HttpServletRequest request, NewReviewDTO dto, Ride ride) {
        Review review = new Review();
        review.setPassenger(userService.getUserFromRequest(request));
        review.setDescription(dto.getDescription());
        review.setDriverRating(dto.getDriverRating());
        review.setVehicleRating(dto.getVehicleRating());
        review.setRide(ride);

        return reviewRepository.save(review);
    }
}
