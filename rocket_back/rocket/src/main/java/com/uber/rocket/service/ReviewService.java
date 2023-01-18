package com.uber.rocket.service;

import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewByPassenger(User passenger) {
        return reviewRepository.findAllByPassenger(passenger);
    }

    public List<Review> getReviewByRide(Ride ride) {
        return reviewRepository.findAllByRide(ride);
    }
}
