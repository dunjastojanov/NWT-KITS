package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {
    @Query("FROM Ride ride WHERE :user MEMBER ride.passengers AND ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByPassengerAndDatePeriod(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end")LocalDateTime end);
    @Query("FROM Ride ride WHERE ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByDatePeriod(@Param("start")LocalDateTime start, @Param("end")LocalDateTime end);
    Page<Ride> findByPassengers(Pageable pageable, User passengers);
    @Query("FROM Ride ride WHERE :user = ride.vehicle.driver AND ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByDriverAndDatePeriod(User user, LocalDateTime start, LocalDateTime end);
    @Query("FROM Ride ride WHERE :vehicle = ride.vehicle")
    Page<Ride> findByVehicleDriver(Pageable pageable, Vehicle vehicle);

}
