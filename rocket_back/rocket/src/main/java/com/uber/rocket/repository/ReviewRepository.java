package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPassenger(User passenger);

    List<Review> findAllByRide(Ride ride);

}
