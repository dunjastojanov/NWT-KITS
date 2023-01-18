package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    @Query("FROM Ride ride JOIN FETCH ride.destinations WHERE :user MEMBER ride.passengers ORDER BY ride.startTime")
    List<Ride> findByPassenger(@Param("user") User user);
    @Query("FROM Ride ride WHERE :user MEMBER ride.passengers AND ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByPassengerAndDatePeriod(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end")LocalDateTime end);
    @Query("FROM Ride ride WHERE ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByDatePeriod(@Param("start")LocalDate start, @Param("end")LocalDate end);

}
