package com.uber.rocket.ride_cancelation.unit;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideCancellation;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.repository.RideCancellationRepository;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.service.NotificationService;
import com.uber.rocket.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RideServiceTest {
    @InjectMocks
    RideService rideService;
    @Mock
    RideRepository rideRepository;
    @Mock
    RideCancellationRepository rideCancellationRepository;
    @Mock
    NotificationService notificationService;
    Ride ride;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);

        User user = new User();
        user.setId(1L);

        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(user);

        ride = new Ride();
        ride.setVehicle(vehicle);
        ride.setStatus(RideStatus.CONFIRMED);
    }

    @Test
    void cancelRide() {
        when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));
        RideCancellation rideCancellation = rideService.cancelRide(1L, "I have some health issues");
        assertEquals("I have some health issues", rideCancellation.getDescription());
        assertEquals(ride, rideCancellation.getRide());
        assertEquals(ride.getDriver(), rideCancellation.getDriver());
    }

    @Test
    void cancelRideRideDoesntExist() {
        when(rideRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            rideService.cancelRide(1L, "I have some health issues");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Ride with given id does not exist.", e.getMessage());
        }
    }

    @Test
    void cancelRideIdNull() {
        try {
            rideService.cancelRide(null, "I have some health issues");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Ride with given id does not exist.", e.getMessage());
        }
    }

    @Test
    void cancelRideReasonIsNull() {
        when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));
        assertThrows(NullPointerException.class, () -> {
            rideService.cancelRide(1L, null);
        });
    }
    @Test
    void cancelRideReasonIsEmpty() {
        when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));

        try {
            rideService.cancelRide(1L, "");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
            assertEquals("Reason cannot be empty.", e.getMessage());
        }

    }

    @Test
    void cancelRideStatusNotConfirmed() {
        ride.setStatus(RideStatus.STARTED);
        when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));
        try {
            rideService.cancelRide(1L, "I have some health issues");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Ride status must be confirmed to be able to cancel.", e.getMessage());
        }
    }

}