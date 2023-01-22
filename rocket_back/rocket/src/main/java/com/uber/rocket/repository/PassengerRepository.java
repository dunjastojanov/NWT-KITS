package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
