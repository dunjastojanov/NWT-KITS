package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.FavouriteRoute;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRouteRepository extends JpaRepository<FavouriteRoute, Long> {

    List<FavouriteRoute> findAllByUser(User user);
    FavouriteRoute findByUserAndRide(User user, Ride ride);
}
