package com.uber.rocket.ride_booking.unit;


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


    @ParameterizedTest
    @MethodSource(value = "getTimePeriod")
    public void findByDriverAndDatePeriod(String start, String end) {
        User driver = userRepository.findById(3L).get();
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        List<Ride> rides = rideRepository.findByDriverAndDatePeriod(driver, startTime, endTime);
        for (Ride ride : rides) {
            assertEquals(driver.getId(), ride.getDriver().getId());
            assertTrue(ride.getStartTime().isAfter(startTime) && ride.getStartTime().isBefore(endTime));
            assertTrue(ride.getEndTime().isAfter(startTime) && ride.getStartTime().isBefore(endTime));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "getTimePeriod")
    public void findByDatePeriod(String start, String end) {
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        List<Ride> rides = rideRepository.findByDatePeriod(startTime, endTime);
        for (Ride ride : rides) {
            assertTrue(ride.getStartTime().isAfter(startTime) && ride.getStartTime().isBefore(endTime));
            assertTrue(ride.getEndTime().isAfter(startTime) && ride.getStartTime().isBefore(endTime));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "getTimePeriod")
    public void shouldFindRidesByPassengerAndDatePeriod(String start, String end) {
        User passenger = userRepository.findById(16L).get();
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        List<Ride> rides = rideRepository.findByDriverAndDatePeriod(passenger, startTime, endTime);
        for (Ride ride : rides) {
            assertTrue(rideContainsPassenger(passenger.getId(), ride.getPassengers().stream().toList()));
            assertTrue(ride.getStartTime().isAfter(startTime) && ride.getStartTime().isBefore(endTime));
            assertTrue(ride.getEndTime().isAfter(startTime) && ride.getStartTime().isBefore(endTime));
        }
    }

    private static Stream<Arguments> getTimePeriod() {
        return Stream.of(
                Arguments.of("2023-01-10 21:20", "2023-01-31 21:20"),
                Arguments.of("2023-01-24 21:20", "2023-01-24 21:20"),
                Arguments.of("2024-01-24 21:20", "2024-01-24 21:20"),
                Arguments.of("2022-01-24 21:20", "2022-01-24 21:20"),
                Arguments.of("2022-01-24 21:20", "2022-01-14 21:20")
        );
    }

    private static boolean rideContainsPassenger(Long userId, List<Passenger> passengers) {
        List<Long> ids = new ArrayList<>();
        for (Passenger passenger : passengers) {
            ids.add(passenger.getUser().getId());
        }
        return ids.contains(userId);
    }
}
