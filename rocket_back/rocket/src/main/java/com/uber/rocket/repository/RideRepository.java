package com.uber.rocket.repository;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
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
    @Query("select ride FROM Ride ride JOIN Passenger p on p member of ride.passengers where p.user = :user AND ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByPassengerAndDatePeriod(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("FROM Ride ride WHERE ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByDatePeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "select r FROM Ride r JOIN Passenger p on p member of r.passengers where p.user = :user and r.vehicle is not null")
    Page<Ride> findByPassengers(Pageable pageable, User user);

    Page<Ride> findAllByStatus(Pageable pageable, RideStatus status);

    @Query("FROM Ride ride WHERE :user = ride.vehicle.driver AND ride.startTime > :start AND ride.endTime < :end ORDER BY ride.startTime")
    List<Ride> findByDriverAndDatePeriod(User user, LocalDateTime start, LocalDateTime end);

    @Query("FROM Ride ride WHERE ride.vehicle.driver = :driver AND ride.status NOT IN (:denied,:ended)")
    List<Ride> findByDriverAndStatus(@Param("driver") User driver, @Param("denied") RideStatus denied, @Param("ended") RideStatus ended);

    @Query("FROM Ride ride WHERE :vehicle = ride.vehicle")
    Page<Ride> findByVehicleDriver(Pageable pageable, Vehicle vehicle);

    @Query("FROM Ride ride WHERE ride.vehicle = :vehicle AND ride.status = com.uber.rocket.entity.ride.RideStatus.CONFIRMED OR ride.status = com.uber.rocket.entity.ride.RideStatus.STARTED")
    Ride findRideByVehicleAndStatus(@Param("vehicle") Vehicle vehicle);

    @Query(value = "select * FROM ride r JOIN passenger p on p.ride_id = r.id where p.user_id = ?1 and r.status != 'DENIED' and r.status != 'ENDED'", nativeQuery = true)
    List<Ride> findByPassengers(Long id);

    @Query("FROM Ride ride WHERE :status = ride.status")
    List<Ride> findByRideStatus(@Param("status") RideStatus rideStatus);

    @Query("FROM Ride ride WHERE ride.vehicle.driver.id=:driverId AND (ride.status = com.uber.rocket.entity.ride.RideStatus.CONFIRMED or ride.status = com.uber.rocket.entity.ride.RideStatus.STARTED)")
    List<Ride> findRidesByDriverIdWhereStatusIsConfirmedOrStarted(@Param("driverId") Long driverId);
}
