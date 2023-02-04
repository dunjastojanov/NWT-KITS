package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.configuration.TestConfig;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.repository.VehicleRepository;
import static org.junit.jupiter.api.Assertions.*;

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

import java.util.List;
import java.util.stream.Stream;

@Import(TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void shouldFindVehicleByDriverId() {
        Vehicle vehicle = vehicleRepository.findFirstByDriverId(3L);
        assertEquals(3L, vehicle.getDriver().getId());
    }

    @Test
    public void shouldFindVehicleByDriverId_DriverDontExist() {
        Vehicle vehicle = vehicleRepository.findFirstByDriverId(99L);
        assertNull(vehicle);
    }
}
