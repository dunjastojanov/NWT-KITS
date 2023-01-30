package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.ride_booking.utils.destination.DestinationCreation;
import com.uber.rocket.ride_booking.utils.destination.RideCreationService;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.RideService;
import com.uber.rocket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class BookingRideServiceTest {
    @Mock
    private RideMapper rideMapperMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private PassengerRepository passengerRepositoryMock;

    @Mock
    private DestinationService destinationServiceMock;

    @Mock
    private RideRepository rideRepositoryMock;

    @InjectMocks
    private RideService rideService;
    private AutoCloseable closeable;

    @BeforeEach
    void initMocks() {
        closeable = openMocks(this);
    }


    @Test
    @DisplayName("Creating ride where rideDTO is okey, every passengers has tokens and destinations are okay")
    void createRideTest1() {
        RideDTO rideDTO = RideCreationService.getRideDTO();
        Ride ride = RideCreationService.getRideEntityWithPassengers();
        when(this.rideMapperMock.mapToEntity(rideDTO)).thenReturn(ride);
        when(this.userServiceMock.checkPassengersTokens(ride)).thenReturn(true);
        when(this.rideRepositoryMock.save(ride)).thenReturn(ride);
        when(this.rideMapperMock.mapToDestination(rideDTO.getDestinations())).thenReturn(DestinationCreation.getDestinationList());
        assertEquals(1L, this.rideService.createRide(RideCreationService.getRideDTO()));
        verify(rideMapperMock, times(1)).mapToEntity(rideDTO);
        verify(rideMapperMock, times(1)).mapToDestination(rideDTO.getDestinations());
        verify(userServiceMock, times(1)).checkPassengersTokens(any(Ride.class));
        verify(rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(destinationServiceMock, times(DestinationCreation.getDestinationList().size())).save(any(Destination.class));
        verify(passengerRepositoryMock, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Creating ride where rideDTO passengers dont have enough money ")
    void createRideTest2() {
        RideDTO rideDTO = RideCreationService.getRideDTO();
        Ride ride = RideCreationService.getRideEntityWithPassengers();
        when(this.rideMapperMock.mapToEntity(rideDTO)).thenReturn(ride);
        when(this.userServiceMock.checkPassengersTokens(ride)).thenReturn(false);
        assertEquals(-1L, this.rideService.createRide(RideCreationService.getRideDTO()));
        verify(rideMapperMock, times(1)).mapToEntity(rideDTO);
        verify(userServiceMock, times(1)).checkPassengersTokens(any(Ride.class));
        verify(rideMapperMock, never()).mapToDestination(anyList());
        verify(rideRepositoryMock, never()).save(any(Ride.class));
        verify(passengerRepositoryMock, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Creating ride where mapper from rideDTO to ride returns null")
    void createRideTest3() {
        RideDTO rideDTO = RideCreationService.getRideDTO();
        Ride ride = RideCreationService.getRideEntityWithPassengers();
        when(this.rideMapperMock.mapToEntity(rideDTO)).thenReturn(ride);
        when(this.userServiceMock.checkPassengersTokens(ride)).thenReturn(false);
        assertEquals(-1L, this.rideService.createRide(RideCreationService.getRideDTO()));
        verify(rideMapperMock, times(1)).mapToEntity(rideDTO);
        verify(userServiceMock, times(1)).checkPassengersTokens(any(Ride.class));
        verify(rideMapperMock, never()).mapToDestination(anyList());
        verify(rideRepositoryMock, never()).save(any(Ride.class));
        verify(passengerRepositoryMock, never()).saveAll(anyList());
    }
    //TODO testiraj svaku zasebnu funkciju


}