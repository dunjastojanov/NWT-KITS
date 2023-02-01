package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.dto.ChangeStatusDTO;
import com.uber.rocket.dto.NotificationDTO;
import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.entity.notification.NotificationType;
import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.ride_booking.utils.destination.DestinationCreation;
import com.uber.rocket.ride_booking.utils.ride.RideCreationService;
import com.uber.rocket.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.uber.rocket.ride_booking.utils.destination.DestinationCreation.getDestination;
import static com.uber.rocket.ride_booking.utils.user.UserCreationService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
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

    @Mock
    private VehicleService vehicleServiceMock;

    @Mock
    private NotificationService notificationServiceMock;

    @Mock
    private SimpMessagingTemplate messagingTemplateMock;

    @InjectMocks
    private RideService rideService;
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
    @DisplayName("Creating ride where rideDTO is okey, every passengers has tokens and destinations are okay")
    void createRideTest1() {
        RideDTO rideDTO = rideCreationService.getRideDTO();
        Ride ride = rideCreationService.getRideEntityWithPassengers();
        when(this.rideMapperMock.mapToEntity(rideDTO)).thenReturn(ride);
        when(this.userServiceMock.checkPassengersTokens(ride)).thenReturn(true);
        when(this.rideRepositoryMock.save(ride)).thenReturn(ride);
        when(this.rideMapperMock.mapToDestination(rideDTO.getDestinations())).thenReturn(DestinationCreation.getDestinationList());
        assertEquals(1L, this.rideService.createRide(rideCreationService.getRideDTO()));
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
        RideDTO rideDTO = rideCreationService.getRideDTO();
        Ride ride = rideCreationService.getRideEntityWithPassengers();
        when(this.rideMapperMock.mapToEntity(rideDTO)).thenReturn(ride);
        when(this.userServiceMock.checkPassengersTokens(ride)).thenReturn(false);
        assertEquals(-1L, this.rideService.createRide(rideCreationService.getRideDTO()));
        verify(rideMapperMock, times(1)).mapToEntity(rideDTO);
        verify(userServiceMock, times(1)).checkPassengersTokens(any(Ride.class));
        verify(rideMapperMock, never()).mapToDestination(anyList());
        verify(rideRepositoryMock, never()).save(any(Ride.class));
        verify(passengerRepositoryMock, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Creating ride where mapper from rideDTO to ride returns null")
    void createRideTest3() {
        RideDTO rideDTO = rideCreationService.getRideDTO();
        Ride ride = rideCreationService.getRideEntityWithPassengers();
        when(this.rideMapperMock.mapToEntity(rideDTO)).thenReturn(ride);
        when(this.userServiceMock.checkPassengersTokens(ride)).thenReturn(false);
        assertEquals(-1L, this.rideService.createRide(rideCreationService.getRideDTO()));
        verify(rideMapperMock, times(1)).mapToEntity(rideDTO);
        verify(userServiceMock, times(1)).checkPassengersTokens(any(Ride.class));
        verify(rideMapperMock, never()).mapToDestination(anyList());
        verify(rideRepositoryMock, never()).save(any(Ride.class));
        verify(passengerRepositoryMock, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Get riding pal if user with passed email doesn't exist")
    void getRidingPalTest1() {
        when(this.userServiceMock.getUserByEmail(anyString())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> this.rideService.getRidingPal(anyString()));
    }

    @Test
    @DisplayName("Get riding pal if user with passed email does exist but it is a driver")
    void getRidingPalTest2() {
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(getDriver());
        assertThrows(RuntimeException.class, () -> this.rideService.getRidingPal(anyString()));
    }

    @Test
    @DisplayName("Get riding pal if user with passed email does exist it is a client but he has a current ride")
    void getRidingPalTest3() {
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(getClient());
        when(this.rideRepositoryMock.findByPassengers(anyLong())).thenReturn(rideCreationService.getRides());
        when(this.rideMapperMock.mapToDto(any(Ride.class))).thenReturn(rideCreationService.getGoodRideDTO());
        assertNull(this.rideService.getRidingPal(anyString()));
    }

    @Test
    @DisplayName("Get riding pal if user with passed email does exist it is a client he has not a current ride")
    void getRidingPalTest4() {
        when(this.userServiceMock.getUserByEmail(anyString())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.findByPassengers(anyLong())).thenReturn(new ArrayList<>());
        assertEquals(getGoodClientDTO().toString(), this.rideService.getRidingPal(anyString()).toString());
    }

    @Test
    @DisplayName("Get user current ride where user is administrator")
    void getUserCurrentRideTest1() {
        assertNull(this.rideService.getUserCurrentRide(getAdminUser()));
    }

    @Test
    @DisplayName("Get user current ride where user is client and has no current ride")
    void getUserCurrentRideTest2() {
        when(this.rideRepositoryMock.findByPassengers(anyLong())).thenReturn(new ArrayList<>());
        assertNull(this.rideService.getUserCurrentRide(getGoodClient()));
    }

    @Test
    @DisplayName("Get user current ride where user is driver and has no current ride")
    void getUserCurrentRideTest3() {
        when(this.rideRepositoryMock.findByPassengers(anyLong())).thenReturn(new ArrayList<>());
        assertNull(this.rideService.getUserCurrentRide(getDriver()));
    }

    @Test
    @DisplayName("Get user current ride where user is client and has current ride")
    void getUserCurrentRideTest4() {
        when(this.rideRepositoryMock.findByPassengers(anyLong())).thenReturn(rideCreationService.getRides());
        when(this.rideMapperMock.mapToDto(any(Ride.class))).thenReturn(rideCreationService.getGoodRideDTO());
        assertEquals(rideCreationService.getGoodRideDTO().toString(), this.rideService.getUserCurrentRide(getGoodClient()).toString());
    }

    @Test
    @DisplayName("Get user current ride where user is driver and has current ride")
    void getUserCurrentRideTest5() {
        when(this.rideRepositoryMock.findByDriver(getDriver())).thenReturn(rideCreationService.getRides());
        when(this.rideMapperMock.mapToDto(any(Ride.class))).thenReturn(rideCreationService.getGoodRideDTO());
        assertEquals(rideCreationService.getGoodRideDTO().toString(), this.rideService.getUserCurrentRide(getDriver()).toString());
    }

    @Test
    @DisplayName("Positive test")
    void createRidingPalTest2() {
        assertEquals(getGoodClientDTO(), rideService.createRidingPal(getGoodClient()));
    }

    @Test
    @DisplayName("Negative test no ride with passed id")
    void getUserCurrentRideByIdTest1() {
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(this.rideService.getUserCurrentRideById(anyLong()));
    }

    @Test
    @DisplayName("Positive test")
    void getUserCurrentRideByIdTest2() {
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.of(rideCreationService.getGoodRideFromRideDTO()));
        when(this.rideMapperMock.mapToDto(any(Ride.class))).thenReturn(rideCreationService.getGoodRideDTO());
        assertEquals(rideCreationService.getGoodRideDTO().toString(), this.rideService.getUserCurrentRideById(anyLong()).toString());
    }


    @Test
    @DisplayName("Negative test no ride with passed id")
    void changeRidePalDriverStatusTest1() {
        when(this.rideRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> this.rideService.changeRidePalDriverStatus("1", getChangeStatus()));
    }


    @Test
    @DisplayName("user is client, ride isn't accepted")
    void changeRidePalDriverStatusTest4() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getGoodRideFromRideDTO()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getGoodRideFromRideDTO());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getGoodRideFromRideDTO().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

    @Test
    @DisplayName("user is client, ride is accepted, but passengers denied")
    void changeRidePalDriverStatusTest5() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersDenied()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersDenied());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersDenied().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

    @Test
    @DisplayName("user is client, ride is accepted, but passengers denied, no destinations")
    void changeRidePalDriverStatusTest6() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersAccepted()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersAccepted());
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenThrow(ArrayIndexOutOfBoundsException.class);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersAccepted().getId()), getGoodChangeStatus()));
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("user is client, ride is accepted, but passengers denied, no available drivers ")
    void changeRidePalDriverStatusTest7() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersAccepted()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersAccepted());
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(getDestination());
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(new ArrayList<Vehicle>());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersAccepted().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

    @Test
    @DisplayName("user is client, ride is accepted, but passengers denied, has available drivers")
    void changeRidePalDriverStatusTest8() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersAccepted()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersAccepted());
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(getDestination());
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(getDriverVehicles());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersAccepted().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
        verify(this.notificationServiceMock, times(1)).addDriverRideRequestNotification(any(User.class), any(Ride.class));
    }


    @Test
    @DisplayName("user is driver, ride isn't accepted")
    void changeRidePalDriverStatusTest13() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getGoodRideFromRideDTO()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getDriver());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getGoodRideFromRideDTO());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getGoodRideFromRideDTO().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

    @Test
    @DisplayName("user is driver, ride is accepted, but passengers denied")
    void changeRidePalDriverStatusTest9() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersDenied()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getGoodClient());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersDenied());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersDenied().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

    @Test
    @DisplayName("user is driver, ride is accepted, but passengers denied, no destinations")
    void changeRidePalDriverStatusTest10() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersAccepted()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getDriver());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersAccepted());
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenThrow(ArrayIndexOutOfBoundsException.class);
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersAccepted().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));

    }

    @Test
    @DisplayName("user is driver, ride is accepted, but passengers denied, no available drivers ")
    void changeRidePalDriverStatusTest11() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersAccepted()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getDriver());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersAccepted());
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(getDestination());
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(new ArrayList<Vehicle>());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersAccepted().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

    @Test
    @DisplayName("user is driver, ride is accepted, but passengers denied, has available drivers")
    void changeRidePalDriverStatusTest12() {
        when(this.rideRepositoryMock.findById(rideCreationService.getGoodRideFromRideDTO().getId())).thenReturn(Optional.of(rideCreationService.getRideIsRequestedPassengersAccepted()));
        when(this.userServiceMock.getById(anyLong())).thenReturn(getDriver());
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(rideCreationService.getRideIsRequestedPassengersAccepted());
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(getDestination());
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(getDriverVehicles());
        rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getRideIsRequestedPassengersAccepted().getId()), getGoodChangeStatus());
        verify(this.rideRepositoryMock, times(1)).findById(anyLong());
        verify(this.userServiceMock, times(1)).getById(anyLong());
        verify(this.rideRepositoryMock, times(1)).save(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).setNotificationAsRead(any(User.class), any(Ride.class), any(NotificationType.class));
    }

//    @Test
//    @DisplayName("Passed parameters are null")
//    void setDriverStatusTest1() {
//        assertThrows(NullPointerException.class, () -> rideService.setDriverStatus(new Ride() ,getChangeStatus(), new User()));
//    }


    @Test
    @DisplayName("User riding status isn't DENIED, riding status is WAITING ")
    void setDriverStatusTest3() {
        Ride ride = rideCreationService.getRideIsRequestedPassengersDenied();
        assertEquals(ride.getStatus(), rideService.setDriverStatus(ride, rideCreationService.getWaitingChangeStatus(), getGoodClient()).getStatus());
    }


    @Test
    @DisplayName("User riding status isn't DENIED, riding status is ACCEPTED, ride isn't now ")
    void setDriverStatusTest5() {
        Ride ride = rideCreationService.getRideIsRequestedPassengersDenied();
        ride.setNow(false);
        when(this.vehicleServiceMock.getVehicleByDriver(any(User.class))).thenReturn(getVehicle());
        assertEquals(RideStatus.SCHEDULED.toString(), rideService.setDriverStatus(ride, rideCreationService.getAcceptedChangeStatus(), any(User.class)).getStatus().toString());
    }

    @Test
    @DisplayName("Ride has no passengers")
    void setClientStatusTest1() {
        Ride ride = rideCreationService.getRideNoPassengers();
        assertEquals(ride, rideService.setClientStatus(ride, getChangeStatus()));
    }

    @Test
    @DisplayName("Ride has passengers but id isn't the same")
    void setClientStatusTest2() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ChangeStatusDTO changeStatusDTO = getGoodChangeStatus();
        changeStatusDTO.setUserId("2");
        Ride ride2 = rideService.setClientStatus(ride, changeStatusDTO);
        assertEquals(ride.getPrice(), ride2.getPrice());
    }


    @Test
    @DisplayName("Ride has passengers , id is the same, userRidingStatus is ACCEPTED")
    void setClientStatusTest4() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ChangeStatusDTO changeStatusDTO = getGoodChangeStatus();
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);
        assertEquals(UserRidingStatus.ACCEPTED, rideService.setClientStatus(ride, changeStatusDTO).getPassengers().stream().toList().get(0).getUserRidingStatus());
    }

    @Test
    @DisplayName("Ride has got a status REQUESTED, but no passengers")
    void allAcceptedRideTest3() {
        Ride ride = rideCreationService.getRideNoPassengers();
        ride.setStatus(RideStatus.DENIED);
        assertTrue(this.rideService.allAcceptedRide(ride));
    }

    @Test
    @DisplayName("Ride has got a status REQUESTED, has passengers but all passengers didnt accepted")
    void allAcceptedRideTest4() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ride.setStatus(RideStatus.REQUESTED);
        ride.getPassengers().stream().toList().forEach(passenger -> {
            passenger.setUserRidingStatus(UserRidingStatus.DENIED);
        });
        assertFalse(this.rideService.allAcceptedRide(ride));
    }

    @Test
    @DisplayName("Ride has got a status REQUESTED, has passengers, all passengers accepted")
    void allAcceptedRideTest5() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ride.setStatus(RideStatus.REQUESTED);
        ride.getPassengers().stream().toList().forEach(passenger -> {
            passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        });
        assertTrue(this.rideService.allAcceptedRide(ride));
    }


    @Test
    @DisplayName("No available vehicles")
    void findAndNotifyDriverTest1() {
        Ride ride = rideCreationService.getGoodRideFromRideDTO();
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(null);
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(new ArrayList<Vehicle>());
        assertFalse(rideService.findAndNotifyDriver(ride));
        verify(this.destinationServiceMock, times(1)).getStartDestinationByRide(any(Ride.class));
    }

    @Test
    @DisplayName("Got available vehicles but no destination ")
    void findAndNotifyDriverTest2() {
        Ride ride = rideCreationService.getGoodRideFromRideDTO();
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(null);
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(getDriverVehicles());
        assertThrows(NullPointerException.class, () -> rideService.findAndNotifyDriver(ride));
        verify(this.destinationServiceMock, times(1)).getStartDestinationByRide(any(Ride.class));
    }

    @Test
    @DisplayName("Got available vehicles which doesn't have current no future ride has destination")
    void findAndNotifyDriverTest3() {
        Ride ride = rideCreationService.getGoodRideFromRideDTO();
        when(this.destinationServiceMock.getStartDestinationByRide(any(Ride.class))).thenReturn(getDestination());
        when(this.vehicleServiceMock.findAvailableDrivers(any(VehicleType.class), anyBoolean(), anyBoolean())).thenReturn(getDriverVehicles());
        when(this.notificationServiceMock.getNotificationsForUser(any(User.class))).thenReturn(new ArrayList<>());
        assertTrue(rideService.findAndNotifyDriver(ride));
        verify(this.destinationServiceMock, times(1)).getStartDestinationByRide(any(Ride.class));
        verify(this.notificationServiceMock, times(1)).addDriverRideRequestNotification(any(User.class), any(Ride.class));
        verify(this.messagingTemplateMock, times(1)).convertAndSendToUser(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Ride has passengers , id is the same, userRidingStatus is DENIED")
    void setClientStatusTest3() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ChangeStatusDTO changeStatusDTO = getGoodChangeStatus();
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);
        assertEquals(RideStatus.DENIED, rideService.setClientStatus(ride, changeStatusDTO).getStatus());
        assertEquals(UserRidingStatus.DENIED, rideService.setClientStatus(ride, changeStatusDTO).getPassengers().stream().toList().get(0).getUserRidingStatus());
    }

    @Test
    @DisplayName("Ride hasn't got a status REQUESTED")
    void allAcceptedRideTest2() {
        Ride ride = rideCreationService.getRideNoPassengers();
        ride.setStatus(RideStatus.SCHEDULED);
        assertEquals(true, this.rideService.allAcceptedRide(ride));
    }

    @Test
    @DisplayName("Negative test no user")
    void changeRidePalDriverStatusTest2() {
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.of(rideCreationService.getGoodRideFromRideDTO()));
        when(this.userServiceMock.getById(anyLong())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> this.rideService.changeRidePalDriverStatus(String.valueOf(rideCreationService.getGoodRideFromRideDTO().getId()), getChangeStatus()));
    }


    @Test
    @DisplayName("User riding status isn't DENIED, riding status is ACCEPTE, ride is now ")
    void setDriverStatusTest4() {
        Ride ride = rideCreationService.getRideIsRequestedPassengersDenied();
        ride.setNow(true);
        assertEquals(RideStatus.CONFIRMED, rideService.setDriverStatus(ride, rideCreationService.getAcceptedChangeStatus(), any(User.class)).getStatus());
    }

    @Test
    @DisplayName("User riding status is DENIED")
    void setDriverStatusTest2() {
        assertEquals(RideStatus.DENIED, rideService.setDriverStatus(rideCreationService.getRideIsRequestedPassengersDenied(), rideCreationService.getDeniedChangeStatus(), getGoodUser()).getStatus());
    }

    @Test
    @DisplayName("No ride with this passed id")
    void createPalsNotifsAndLookForDriverTest1() {
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        rideService.createPalsNotifsAndLookForDriver(1L);
        verify(rideRepositoryMock, times(1)).findById(anyLong());
        verify(notificationServiceMock, never()).addPassengerRequestNotification(any(User.class), any(Ride.class));
    }

    @Test
    @DisplayName("driver isn't set while adding pals")
    void createPalsNotifsAndLookForDriverTest2() {
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.of(rideCreationService.getRideMorePassengerYesMoney()));
        rideService.createPalsNotifsAndLookForDriver(1L);
        verify(rideRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Drive is set while adding pals")
    void createPalsNotifsAndLookForDriverTest3() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ride.getPassengers().stream().toList().forEach(passenger -> passenger.setUserRidingStatus(UserRidingStatus.DENIED));
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.of(ride));
        rideService.createPalsNotifsAndLookForDriver(1L);
        verify(rideRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("change riding status but cant fetch ride")
    void changeRideStatusTest1() {
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(rideService.changeRideStatus(1L, any(RideStatus.class)));
    }

    @Test
    @DisplayName("change riding status, fetched ride,setting STARTED status ")
    void changeRideStatusTest2() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        ride.getPassengers().stream().toList().forEach(passenger -> passenger.setUserRidingStatus(UserRidingStatus.DENIED));
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.of(ride));
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(ride);
        assertNull(rideService.changeRideStatus(1L, RideStatus.STARTED));
        verify(this.notificationServiceMock, never()).addRideReviewNotification(any(User.class), any(Ride.class));
    }

    @Test
    @DisplayName("change riding status, fetched ride,setting ENDED status ")
    void changeRideStatusTest3() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        ride.getPassengers().stream().toList().forEach(passenger -> passenger.setUserRidingStatus(UserRidingStatus.DENIED));
        when(this.rideRepositoryMock.findById(anyLong())).thenReturn(Optional.of(ride));
        when(this.rideRepositoryMock.save(any(Ride.class))).thenReturn(ride);
        assertNull(rideService.changeRideStatus(1L, RideStatus.ENDED));
        verify(this.notificationServiceMock, times(ride.getPassengers().size())).addRideReviewNotification(any(User.class), any(Ride.class));
    }
}
