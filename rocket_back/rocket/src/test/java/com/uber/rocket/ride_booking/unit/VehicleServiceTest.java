package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.VehicleRepository;
import com.uber.rocket.ride_booking.utils.user.UserCreationService;
import com.uber.rocket.service.VehicleService;
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.uber.rocket.ride_booking.utils.user.UserCreationService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepositoryMock;

    @InjectMocks
    private VehicleService vehicleService;
    private AutoCloseable closeable;

    @BeforeEach
    void initMocks() {
        closeable = openMocks(this);
    }

    @Test
    @DisplayName("Negative test get Vehicle by Driver")
    void getVehicleByDriverTest1() {
        when(this.vehicleRepositoryMock.findFirstByDriver(any(User.class))).thenReturn(null);
        assertNull(this.vehicleService.getVehicleByDriver(getDriver()));
    }

    @Test
    @DisplayName("Positive test get Vehicle by Driver")
    void getVehicleByDriverTest2() {
        when(this.vehicleRepositoryMock.findFirstByDriver(any(User.class))).thenReturn(getVehicle());
        assertEquals(getVehicle(), this.vehicleService.getVehicleByDriver(getDriver()));
    }

}
