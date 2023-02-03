package com.uber.rocket.ride_execution;

import com.uber.rocket.configuration.TestConfig;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.repository.UserRepository;
import com.uber.rocket.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Import(TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    public void shouldFindRidesWithDriverIdWhereStatusConfirmedOrStarted() {
        List<Ride> rides = rideRepository.findRidesByDriverIdWhereStatusIsConfirmedOrStarted(5L);
        for (Ride ride : rides) {
            assertEquals(5L, ride.getDriver().getId());
            assertTrue(ride.getStatus() == RideStatus.CONFIRMED || ride.getStatus() == RideStatus.STARTED);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "getRideStatus")
    public void shouldFindRidesByStatus(RideStatus status) {
        List<Ride> rides = rideRepository.findByRideStatus(status);
        for (Ride ride : rides) {
            assertEquals(status, ride.getStatus());
        }
    }

    private static List<RideStatus> getRideStatus() {
        return List.of(RideStatus.DENIED,
                RideStatus.CONFIRMED,
                RideStatus.REQUESTED,
                RideStatus.ENDED,
                RideStatus.SCHEDULED,
                RideStatus.STARTED);
    }

    @Test
    public void shouldFindRidesByDriverAndRideStatusNotDeniedOrEnded() {
        User driver = userRepository.findById(3L).get();
        List<Ride> rides = rideRepository.findByDriver(driver);
        for (Ride ride : rides) {
            assertEquals(3L, ride.getDriver().getId());
            assertTrue(ride.getStatus() != RideStatus.DENIED && ride.getStatus() != RideStatus.ENDED);
        }
    }

    @Test
    public void shouldFindRidesByPassengersAndRideStatusNotDeniedOrEnded() {
        List<Ride> rides = rideRepository.findByPassengers(5L);
        for (Ride ride: rides) {
            assertTrue(rideContainsPassenger(5L, ride.getPassengers().stream().toList()));
            assertTrue(ride.getStatus() != RideStatus.DENIED && ride.getStatus() != RideStatus.ENDED);
        }
    }
    private static boolean rideContainsPassenger(Long userId, List<Passenger> passengers) {
        List<Long> ids = new ArrayList<>();
        for (Passenger passenger : passengers) {
            ids.add(passenger.getUser().getId());
        }
        return ids.contains(userId);
    }

    @Test
    public void shouldFindRidesByVehicleAndStatus() {
        Vehicle vehicle = vehicleRepository.findById(6L).get();
        List<Ride> rides = rideRepository.findRideByVehicleAndStatus(vehicle);
        for (Ride ride: rides) {
            assertEquals(vehicle.getId(), ride.getVehicle().getId());
            assertTrue(ride.getStatus() == RideStatus.STARTED || ride.getStatus() == RideStatus.CONFIRMED || ride.getStatus() == RideStatus.SCHEDULED);
        }
    }

    @Test
    public void shouldFindRidesByDriverAndStatus() {
        User driver = userRepository.findById(3L).get();
        List<Ride> rides = rideRepository.findByDriverAndStatus(driver, RideStatus.DENIED, RideStatus.ENDED);
        for (Ride ride: rides) {
            assertEquals(driver.getId(), ride.getDriver().getId());
            assertTrue(ride.getStatus() != RideStatus.DENIED && ride.getStatus() != RideStatus.ENDED);
        }
    }
}
