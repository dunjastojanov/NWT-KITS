package com.uber.rocket.ride_execution;

import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.DriverReport;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.repository.DriverReportRepository;
import com.uber.rocket.service.UserService;
import com.uber.rocket.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class VehicleServiceTest {

    @Mock
    UserService userService;
    @Mock
    HttpServletRequest request;

    @Mock
    DriverReportRepository driverReportRepository;

    @InjectMocks
    VehicleService vehicleService;

    User driver;
    User passenger;
    DriverReport driverReport;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);

        driver = new User();
        driver.setId(1L);

        passenger = new User();
        passenger.setId(2L);

        driverReport = new DriverReport();
        driverReport.setDriver(driver);
        driverReport.setPassenger(passenger);

    }

    @Test
    public void reportDriver_Success() {
        Long id = 1L;
        when(userService.getUserFromRequest(request)).thenReturn(passenger);
        when(userService.getById(id)).thenReturn(driver);

        vehicleService.reportDriver(request, id);
        verify(userService).getById(id);
        verify(userService).getUserFromRequest(request);
        verify(driverReportRepository).save(driverReport);
    }

    @Test
    public void reportDriver_PassengerNotFound() {
        Long id = 1L;
        when(userService.getUserFromRequest(request)).thenThrow(new RuntimeException("User not found"));
        when(userService.getById(id)).thenReturn(driver);

        try{
            vehicleService.reportDriver(request, id);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("User not found", e.getMessage());
        }
        verify(userService).getUserFromRequest(request);
        verify(userService, never()).getById(id);
        verify(driverReportRepository, never()).save(driverReport);
    }

    @Test
    public void reportDriver_DriverNotFound() {
        Long id = 1L;
        when(userService.getUserFromRequest(request)).thenReturn(passenger);
        when(userService.getById(id)).thenThrow(new RuntimeException("User not found"));
        try{
            vehicleService.reportDriver(request, id);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("User not found", e.getMessage());
        }
        verify(userService).getUserFromRequest(request);
        verify(userService).getById(id);
        verify(driverReportRepository, never()).save(driverReport);
    }


}