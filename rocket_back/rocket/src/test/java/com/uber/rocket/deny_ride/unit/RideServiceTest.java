package com.uber.rocket.deny_ride.unit;

import com.uber.rocket.dto.ChangeStatusDTO;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.Role;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.mapper.FavouriteRouteMapper;
import com.uber.rocket.repository.FavouriteRouteRepository;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.repository.RideCancellationRepository;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RideServiceTest {
    @Mock
    RideRepository rideRepository;
    @Mock
    PassengerRepository passengerRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    UserService userService;
    @Mock
    VehicleService vehicleService;
    @Mock
    DestinationService destinationService;

    @Mock
    SimpMessagingTemplate messagingTemplate;
    @InjectMocks
    RideService rideService;
    Ride ride;
    User user;
    Passenger passenger;
    User client;
    Vehicle vehicle;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);

        user = new User();
        user.setId(1L);

        client = new User();
        client.setId(2L);
        client.setEmail("client@gmail.com");

        passenger = new Passenger();

        vehicle = new Vehicle();
        vehicle.setDriver(user);
        vehicle.setId(1L);
        vehicle.setStatus(VehicleStatus.ACTIVE);

        ride = new Ride();
        ride.setId(1L);

        ride.setStatus(RideStatus.REQUESTED);

    }
    @Test
    public void testChangeRidePalDriverStatus_WhenPalDeniesRide() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("CLIENT");
        user.setRoles(Collections.singletonList(role));

        List<Passenger> passengers = new ArrayList<>();

        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.WAITING);
        passengers.add(passenger);

        Passenger passenger1 = new Passenger();
        passenger1.setUser(client);
        passenger1.setUserRidingStatus(UserRidingStatus.ACCEPTED);

        passengers.add(passenger1);
        ride.setPassengers(passengers);

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);
        verify(vehicleService, never()).findAvailableDrivers(any(), anyBoolean(), anyBoolean());
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNotNull(result);
        assertEquals(RideStatus.DENIED, result.getStatus());
        assertNull(result.getVehicle());
        assertEquals(UserRidingStatus.DENIED, ride.getPassengers().stream().toList().get(0).getUserRidingStatus());
        assertEquals(UserRidingStatus.ACCEPTED, ride.getPassengers().stream().toList().get(1).getUserRidingStatus());
    }

    @Test
    public void testChangeRidePalDriverStatus_UserIdEmptyString() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("");
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("DRIVER");
        user.setRoles(Collections.singletonList(role));

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(anyLong())).thenReturn(user);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);


        assertThrows(NumberFormatException.class, () -> rideService.changeRidePalDriverStatus(rideId, changeStatusDTO));

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService, never()).getById(anyLong());
        verify(notificationService, never()).setNotificationAsRead(user, ride, NotificationType.DRIVER_RIDE_REQUEST);
        verify(rideRepository, never()).save(ride);
        verify(vehicleService, never()).findAvailableDrivers(any(), anyBoolean(), anyBoolean());
        verify(destinationService, never()).getStartDestinationByRide(ride);
    }



    @Test
    public void testChangeRidePalDriverStatus_WhenDriverAcceptsRideRequestForNow() {
        ride.setNow(true);
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("DRIVER");
        user.setRoles(Collections.singletonList(role));

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);
        verify(vehicleService, never()).findAvailableDrivers(any(), anyBoolean(), anyBoolean());
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNotNull(result);
        assertEquals(RideStatus.CONFIRMED, result.getStatus());
        assertNotEquals(RideStatus.DENIED, result.getStatus());
        assertNotNull(result.getVehicle());
    }
    @Test
    public void testChangeRidePalDriverStatus_WhenDriverDeniesRideRequest() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("DRIVER");
        user.setRoles(Collections.singletonList(role));

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);
        verify(vehicleService, never()).findAvailableDrivers(any(), anyBoolean(), anyBoolean());
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNotNull(result);
        assertEquals(RideStatus.DENIED, result.getStatus());
        assertNull(result.getVehicle());
    }

    @Test
    public void testChangeRidePalDriverStatus_NoRidePresent() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);

        Optional<Ride> rideOpt = Optional.empty();

        Role role = new Role();
        role.setRole("DRIVER");
        user.setRoles(Collections.singletonList(role));

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);

        try{
            rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);
        } catch (Exception e) {
            assertEquals("Ride not found", e.getMessage());
        }

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(notificationService, never()).setNotificationAsRead(user, ride, NotificationType.DRIVER_RIDE_REQUEST);
        verify(rideRepository, never()).save(ride);
    }


    @Test
    public void testChangeRidePalDriverStatus_WhenDriverAcceptsRideRequestForFuture() {
        ride.setNow(false);
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("DRIVER");
        user.setRoles(Collections.singletonList(role));

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);
        verify(vehicleService, never()).findAvailableDrivers(any(), anyBoolean(), anyBoolean());
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNotNull(result);
        assertEquals(RideStatus.SCHEDULED, result.getStatus());
        assertNotEquals(RideStatus.DENIED, result.getStatus());
        assertNotNull(result.getVehicle());
    }

    @Test
    public void testChangeRidePalDriverStatus_WhenAllPalsAcceptRideDriverFound() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("CLIENT");
        user.setRoles(Collections.singletonList(role));

        List<Passenger> passengers = new ArrayList<>();

        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.WAITING);
        passengers.add(passenger);
        ride.setPassengers(passengers);

        Destination destination = new Destination();
        destination.setLongitude(1.1);
        destination.setLatitude(2.2);

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);
        when(vehicleService.findAvailableDrivers(any(), anyBoolean(), anyBoolean())).thenReturn(List.of(vehicle));
        when(destinationService.getStartDestinationByRide(ride)).thenReturn(destination);

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);

        assertNotNull(result);
        assertNotEquals(RideStatus.DENIED, result.getStatus());
        assertEquals(RideStatus.REQUESTED, result.getStatus());
        assertNull(result.getVehicle());
        assertEquals(UserRidingStatus.ACCEPTED, ride.getPassengers().stream().toList().get(0).getUserRidingStatus());
    }

    @Test
    public void testChangeRidePalDriverStatus_WhenAllPalsAcceptRideDriverNotFound() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("CLIENT");
        user.setRoles(Collections.singletonList(role));

        List<Passenger> passengers = new ArrayList<>();

        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.WAITING);
        passengers.add(passenger);
        ride.setPassengers(passengers);

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);
        when(vehicleService.findAvailableDrivers(any(), anyBoolean(), anyBoolean())).thenReturn(new ArrayList<>());

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);

        assertNotNull(result);
        assertEquals(RideStatus.DENIED, result.getStatus());
        assertNull(result.getVehicle());
        assertEquals(UserRidingStatus.ACCEPTED, ride.getPassengers().stream().toList().get(0).getUserRidingStatus());
    }

    @Test
    public void testChangeRidePalDriverStatus_WhenNotAllPalsAcceptRide() {
        String rideId = "1";
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setUserId("1");
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);

        Optional<Ride> rideOpt = Optional.of(ride);

        Role role = new Role();
        role.setRole("CLIENT");
        user.setRoles(Collections.singletonList(role));

        List<Passenger> passengers = new ArrayList<>();

        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.WAITING);
        passengers.add(passenger);

        Passenger passengerWaiting = new Passenger();
        passengerWaiting.setUser(client);
        passengerWaiting.setUserRidingStatus(UserRidingStatus.WAITING);
        passengers.add(passengerWaiting);

        ride.setPassengers(passengers);

        when(rideRepository.findById(Long.parseLong(rideId))).thenReturn(rideOpt);
        when(userService.getById(Long.parseLong(changeStatusDTO.getUserId()))).thenReturn(user);
        doNothing().when(this.userService).passengerTokensWithdraw(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(vehicleService.getVehicleByDriver(user)).thenReturn(vehicle);

        Ride result = rideService.changeRidePalDriverStatus(rideId, changeStatusDTO);

        verify(rideRepository).findById(Long.parseLong(rideId));
        verify(userService).getById(Long.parseLong(changeStatusDTO.getUserId()));
        verify(rideRepository).save(ride);
        verify(vehicleService, never()).findAvailableDrivers(any(), anyBoolean(), anyBoolean());
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNotNull(result);
        assertNotEquals(RideStatus.DENIED, result.getStatus());
        assertNull(result.getVehicle());
        assertEquals(UserRidingStatus.ACCEPTED, ride.getPassengers().stream().toList().get(0).getUserRidingStatus());
        assertEquals(UserRidingStatus.WAITING, ride.getPassengers().stream().toList().get(1).getUserRidingStatus());
    }

    //SET DRIVER STATUS
    //SET CLIENT STATUS
    //FIND ANF NOTIFY DRIVER
    //allAcceptedRide
}