package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.repository.DestinationRepository;
import com.uber.rocket.ride_booking.utils.ride.RideCreationService;
import com.uber.rocket.service.DestinationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.validation.constraints.Null;
import java.util.ArrayList;

import static com.uber.rocket.ride_booking.utils.destination.DestinationCreation.getDestinationList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class DestinationServiceTest {
    @InjectMocks
    private DestinationService destinationService;

    @Mock
    private DestinationRepository destinationRepositoryMock;
    private AutoCloseable closeable;

    private RideCreationService rideCreationService;

    @BeforeEach
    void initMocks() throws Exception {
        closeable = openMocks(this);
        rideCreationService = new RideCreationService();
    }


    @AfterEach
    void afterMethod() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("No destinations for Ride")
    void getStartDestinationByRideTest1() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        when(destinationRepositoryMock.findAllByRide(any(Ride.class))).thenReturn(new ArrayList<>());
        assertThrows(IndexOutOfBoundsException.class, () -> this.destinationService.getStartDestinationByRide(ride));
    }

    @Test
    @DisplayName("Positive test for getting start destination for ride")
    void getStartDestinationByRideTest2() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        when(destinationRepositoryMock.findAllByRide(any(Ride.class))).thenReturn(getDestinationList());
        assertEquals(getDestinationList().get(0).toString(), destinationService.getStartDestinationByRide(ride).toString());
    }
}
