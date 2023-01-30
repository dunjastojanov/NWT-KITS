package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.repository.FavouriteRouteRepository;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.ride_booking.utils.destination.RideCreationService;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RideMapperTest {

    @Mock
    UserService userService;

    @Mock
    DestinationService destinationService;

    @Mock
    PassengerRepository passengerRepositoryMock;

    @Mock
    FavouriteRouteRepository favouriteRouteRepositoryMock;

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
        when(this.userService.getUserByEmail(anyString())).thenThrow(new RuntimeException("User not found"));
        assertThrows(RuntimeException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userService, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity no ridding pals")
    void mapToEntityTest2() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNoRidingPals();
        when(this.userService.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userService, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity no vehicle")
    void mapToEntityTest3() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNoVehicle();
        when(this.userService.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userService, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity no vehicle features")
    void mapToEntityTest4() {
        RideDTO rideDTO = RideCreationService.getRideDTOWithNoVehicleFeatures();
        when(this.userService.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userService, times(1)).getUserByEmail(anyString());
    }

    @Test
    @DisplayName("Negative test for mapping RideDTO to entity ")
    void mapToEntityTest5() {
        RideDTO rideDTO = RideCreationService.getGoodRideDTO();
        when(this.userService.getUserByEmail(anyString())).thenReturn(RideCreationService.getGoodUser());
        assertThrows(NullPointerException.class, () -> rideMapper.mapToEntity(rideDTO));
        verify(userService, times(1)).getUserByEmail(anyString());
    }


}