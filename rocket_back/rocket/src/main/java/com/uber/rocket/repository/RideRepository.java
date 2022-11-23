package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    @Query("FROM Ride ride WHERE :user MEMBER ride.passengers")
    List<Ride> findByPassenger(@Param("user") User user);

}
