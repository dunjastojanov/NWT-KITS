package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.dto.DestinationDTO;
import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.ride_booking.utils.ride.RideCreationService;
import com.uber.rocket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.format.DateTimeParseException;
import java.util.List;

import static com.uber.rocket.ride_booking.utils.destination.DestinationCreation.getDestinationDTOList;
import static com.uber.rocket.ride_booking.utils.destination.DestinationCreation.getDestinationListForMapping;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RideMapperTest {

    @Mock
    UserService userServiceMock;

    @InjectMocks
    private RideMapper rideMapper;
    private AutoCloseable closeable;

    @BeforeEach
    void initMocks() {
        closeable = openMocks(this);
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity user not found")
    void mapToEntityTest1() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNonexistentUser();
        when(this.userServiceMock.getUserByEmail(anyString())).thenThrow(new RuntimeException("User not found"));
        assertThrows(RuntimeException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userServiceMock, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity no ridding pals")
    void mapToEntityTest2() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNoRidingPals();
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userServiceMock, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity no vehicle")
    void mapToEntityTest3() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNoVehicle();
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userServiceMock, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity no vehicle features null element")
    void mapToEntityTest4() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNoVehicleFeatures();
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userServiceMock, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test mapping ride because of invalid time string")
    void mapToEntityTest5() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithBadTimeString();
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(DateTimeParseException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userServiceMock, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Positive test map rideDTO to ride")
    void mapToEntityTest6() {
        RideDTO rideDTO = RideCreationService.getGoodRideDTO();
        Ride ride = RideCreationService.getGoodRideFromRideDTO();
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertEquals(ride.toString(), rideMapper.mapToEntity(rideDTO).toString());
        verify(userServiceMock, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Positive test map rideDTO to ride")
    void mapToDestinationTest1() {
        List<DestinationDTO> destination = getDestinationDTOList();
        List<Destination> destinations = getDestinationListForMapping();
        assertEquals(destinations.toString(), rideMapper.mapToDestination(destination).toString());
    }

}