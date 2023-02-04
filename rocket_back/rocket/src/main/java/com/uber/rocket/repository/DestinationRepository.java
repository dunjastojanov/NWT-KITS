package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    public List<Destination> findAllByRide(Ride ride);
}
