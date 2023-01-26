package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.RideCancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideCancellationRepository extends JpaRepository<RideCancellation, Long> {
}
