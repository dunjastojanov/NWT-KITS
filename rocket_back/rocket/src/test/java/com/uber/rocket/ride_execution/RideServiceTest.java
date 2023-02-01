package com.uber.rocket.ride_execution;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.mapper.FavouriteRouteMapper;
import com.uber.rocket.repository.FavouriteRouteRepository;
import com.uber.rocket.repository.RideCancellationRepository;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RideServiceTest {
    @Mock
    RideRepository rideRepository;
    @Mock
    RideCancellationRepository rideCancellationRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    UserService userService;
    @Mock
    VehicleService vehicleService;
    @Mock
    DestinationService destinationService;
    @Mock
    FavouriteRouteMapper favouriteRouteMapper;
    @Mock
    FavouriteRouteRepository favouriteRouteRepository;
    @Mock
    HttpServletRequest request;
    @Mock
    SimpMessagingTemplate messagingTemplate;
    @InjectMocks
    RideService rideService;
    Ride ride;
    Passenger passenger;
    User client;
    FavouriteRoute favouriteRoute;

    Vehicle vehicle;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);

        User user = new User();
        user.setId(1L);

        client = new User();
        client.setId(2L);
        client.setEmail("client@gmail.com");

        passenger = new Passenger();
        passenger.setUser(client);
        passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);

        vehicle = new Vehicle();
        vehicle.setDriver(user);
        vehicle.setId(1L);
        vehicle.setStatus(VehicleStatus.ACTIVE);

        ride = new Ride();
        ride.setId(1L);
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setVehicle(vehicle);
        ride.setStatus(RideStatus.CONFIRMED);

        favouriteRoute = new FavouriteRoute();
        favouriteRoute.setRide(ride);
        favouriteRoute.setUser(client);
    }

    @Test
    public void changeRideStatus_rideIsPresent_statusIsChangedToStarted() {
        Long id = 1L;
        RideStatus status = RideStatus.STARTED;
        Optional<Ride> rideOpt = Optional.of(ride);

        when(rideRepository.findById(id)).thenReturn(rideOpt);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(notificationService.getNotificationsForUser(client)).thenReturn(new ArrayList<>());
        RideDTO result = rideService.changeRideStatus(id, status);

        verify(rideRepository).findById(id);
        verify(rideRepository).save(ride);
        verify(notificationService, never()).addRideReviewNotification(client, ride);
        verify(messagingTemplate, never()).convertAndSendToUser(client.getEmail(), "/queue/notifications", notificationService.getNotificationsForUser(client));
        assertEquals(status, ride.getStatus());
        assertNotNull(ride.getStartTime());
        assertNull(ride.getEndTime());
        assertNull(result);
    }

    @Test
    public void changeRideStatus_statusIsChangedToEnded() {
        Long id = 1L;
        RideStatus status = RideStatus.ENDED;
        Optional<Ride> rideOpt = Optional.of(ride);

        when(rideRepository.findById(id)).thenReturn(rideOpt);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(notificationService.getNotificationsForUser(client)).thenReturn(new ArrayList<>());

        RideDTO result = rideService.changeRideStatus(id, status);

        verify(rideRepository).findById(id);
        verify(rideRepository).save(ride);
        verify(notificationService).addRideReviewNotification(client, ride);
        verify(messagingTemplate).convertAndSendToUser(client.getEmail(), "/queue/notifications", notificationService.getNotificationsForUser(client));
        assertEquals(status, ride.getStatus());
        assertNull(ride.getStartTime());
        assertNotNull(ride.getEndTime());
        assertNull(result);
    }

    @Test
    public void changeRideStatus_statusIsChangedToOtherValue() {
        Long id = 1L;
        RideStatus status = RideStatus.REQUESTED;

        Optional<Ride> rideOpt = Optional.of(ride);
        when(rideRepository.findById(id)).thenReturn(rideOpt);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(notificationService.getNotificationsForUser(client)).thenReturn(new ArrayList<>());

        RideDTO result = rideService.changeRideStatus(id, status);

        verify(rideRepository).findById(id);
        verify(rideRepository).save(ride);
        verify(notificationService, never()).addRideReviewNotification(client, ride);
        verify(messagingTemplate, never()).convertAndSendToUser(client.getEmail(), "/queue/notifications", notificationService.getNotificationsForUser(client));
        assertEquals(status, ride.getStatus());
        assertNull(ride.getStartTime());
        assertNull(ride.getEndTime());
        assertNull(result);
    }

    @Test
    public void changeRideStatus_rideIsNotPresent() {
        Long id = 1L;
        RideStatus status = RideStatus.STARTED;

        Optional<Ride> rideOpt = Optional.empty();
        when(rideRepository.findById(id)).thenReturn(rideOpt);

        RideDTO result = rideService.changeRideStatus(id, status);

        verify(rideRepository).findById(id);
        verify(rideRepository, never()).save(ride);
        assertNull(result);
    }

    @Test
    void testAddFavouriteRoute_Success() {
        Long id = 1L;
        Optional<Ride> rideOpt = Optional.of(ride);
        when(rideRepository.findById(id)).thenReturn(rideOpt);
        when(userService.getUserFromRequest(request)).thenReturn(client);
        when(userService.getUserByEmail(client.getEmail())).thenReturn(client);
        when(favouriteRouteRepository.save(any(FavouriteRoute.class))).thenReturn(favouriteRoute);

        FavouriteRouteDTO favouriteRouteDTO = new FavouriteRouteDTO();
        when(favouriteRouteMapper.mapToDto(favouriteRoute)).thenReturn(favouriteRouteDTO);

        FavouriteRouteDTO result = rideService.addFavouriteRoute(request, id);

        assertEquals(favouriteRouteDTO, result);
        verify(userService).getUserFromRequest(request);
        verify(userService).getUserByEmail(client.getEmail());
        verify(favouriteRouteRepository).save(any(FavouriteRoute.class));
        verify(favouriteRouteMapper).mapToDto(favouriteRoute);
    }

    @Test
    void testAddFavouriteRoute_RideDoesNotExist() {
        when(userService.getUserFromRequest(request)).thenReturn(client);
        try{
            rideService.addFavouriteRoute(request, 1L);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("Ride with given id does not exist.", e.getMessage());
        }
        verify(userService).getUserFromRequest(request);
        verify(userService, never()).getUserByEmail(client.getEmail());
    }

    @Test
    void testAddFavouriteRoute_UserNotFound() {
        Long id = 1L;
        Optional<Ride> rideOpt = Optional.of(ride);
        when(rideRepository.findById(id)).thenReturn(rideOpt);
        when(userService.getUserFromRequest(request)).thenReturn(client);
        when(userService.getUserByEmail(client.getEmail())).thenThrow(new RuntimeException("User not found"));
        try{
            rideService.addFavouriteRoute(request, id);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("User not found", e.getMessage());
        }
        verify(userService).getUserFromRequest(request);
        verify(userService).getUserByEmail(client.getEmail());
    }

    @Test
    public void testGetRideForSimulation_WhenVehiclePresent() {
        Long vehicleId = 1L;
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);

        ride.setRouteLocation("encodedRouteLocation");
        List<Ride> rides = Arrays.asList(ride);

        Destination destination = new Destination();
        destination.setLongitude(1);
        destination.setLatitude(2);
        List<Double> longLat = Arrays.asList(1.0, 2.0);

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);
        when(destinationService.getStartDestinationByRide(ride)).thenReturn(destination);

        RideSimulationDTO rsDTO = rideService.getRideForSimulation(vehicleId);

        verify(vehicleService).getVehicleById(vehicleId);
        verify(vehicleService).save(vehicle);
        verify(rideRepository).findRideByVehicleAndStatus(vehicle);
        verify(destinationService).getStartDestinationByRide(ride);

        assertNotNull(rsDTO);
        assertNotNull(rsDTO.getVehicleStatus());
        assertNotNull(rsDTO.getRide());
        assertNotNull(rsDTO.getRide().getStatus());
        assertNotNull(rsDTO.getRide().getRouteCoordinates());
        assertNotNull(rsDTO.getRide().getDestination());
        assertEquals(longLat, rsDTO.getRide().getDestination());
    }

    @Test
    public void testGetRideForSimulation_WhenVehicleIsNotPresent() {
        Long vehicleId = 1L;
        Optional<Vehicle> vehicleOpt = Optional.empty();

        ride.setRouteLocation("encodedRouteLocation");
        List<Ride> rides = Arrays.asList(ride);

        Destination destination = new Destination();
        destination.setLongitude(1);
        destination.setLatitude(2);

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);
        when(destinationService.getStartDestinationByRide(ride)).thenReturn(destination);

        RideSimulationDTO rsDTO = rideService.getRideForSimulation(vehicleId);

        verify(vehicleService).getVehicleById(vehicleId);
        verify(vehicleService, never()).save(vehicle);
        verify(rideRepository, never()).findRideByVehicleAndStatus(vehicle);
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNull(rsDTO);
    }

    @Test
    public void testGetRideForSimulation_WhenNoRideForVehicle() {
        Long vehicleId = 1L;
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);
        List<Ride> rides = Arrays.asList(ride);

        Destination destination = new Destination();
        destination.setLongitude(1);
        destination.setLatitude(2);

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);
        when(destinationService.getStartDestinationByRide(ride)).thenReturn(destination);

        Vehicle newVehicle = new Vehicle();
        newVehicle.setId(2L);
        ride.setVehicle(newVehicle);
        ride.setRouteLocation("encodedRouteLocation");

        RideSimulationDTO rsDTO = rideService.getRideForSimulation(vehicleId);

        verify(vehicleService).getVehicleById(vehicleId);
        verify(vehicleService).save(vehicle);
        verify(rideRepository).findRideByVehicleAndStatus(vehicle);
        verify(destinationService, never()).getStartDestinationByRide(ride);

        assertNotNull(rsDTO);
        assertNotNull(rsDTO.getVehicleStatus());
        assertNull(rsDTO.getRide());
    }

    @Test
    public void testGetRideForSimulation_WhenVehicleWithRideConfirmedStatusInsteadScheduled() {
        Long vehicleId = 1L;
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);

        Ride scheduledRide = new Ride();
        scheduledRide.setVehicle(vehicle);
        scheduledRide.setStatus(RideStatus.SCHEDULED);
        ride.setRouteLocation("encodedRouteLocation");
        List<Ride> rides = Arrays.asList(ride, scheduledRide);

        Destination destination = new Destination();
        destination.setLongitude(1);
        destination.setLatitude(2);
        List<Double> longLat = Arrays.asList(1.0, 2.0);

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);
        when(destinationService.getStartDestinationByRide(ride)).thenReturn(destination);

        RideSimulationDTO rsDTO = rideService.getRideForSimulation(vehicleId);

        verify(vehicleService).getVehicleById(vehicleId);
        verify(vehicleService).save(vehicle);
        verify(rideRepository).findRideByVehicleAndStatus(vehicle);
        verify(destinationService).getStartDestinationByRide(ride);

        assertNotNull(rsDTO);
        assertNotNull(rsDTO.getVehicleStatus());
        assertNotNull(rsDTO.getRide());
        assertEquals(RideStatus.CONFIRMED ,rsDTO.getRide().getStatus());
        assertNotNull(rsDTO.getRide().getRouteCoordinates());
        assertNotNull(rsDTO.getRide().getDestination());
        assertEquals(longLat, rsDTO.getRide().getDestination());
    }

    @Test
    public void testGetRideForSimulation_WhenVehicleWithRideScheduled() {
        Long vehicleId = 1L;
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);

        Ride scheduledRide = new Ride();
        scheduledRide.setVehicle(vehicle);
        scheduledRide.setStatus(RideStatus.SCHEDULED);
        scheduledRide.setRouteLocation("encodedRouteLocation");
        List<Ride> rides = Arrays.asList(scheduledRide);

        Destination destination = new Destination();
        destination.setLongitude(1);
        destination.setLatitude(2);
        List<Double> longLat = Arrays.asList(1.0, 2.0);

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);
        when(destinationService.getStartDestinationByRide(scheduledRide)).thenReturn(destination);

        RideSimulationDTO rsDTO = rideService.getRideForSimulation(vehicleId);

        verify(vehicleService).getVehicleById(vehicleId);
        verify(vehicleService).save(vehicle);
        verify(rideRepository).findRideByVehicleAndStatus(vehicle);
        verify(destinationService).getStartDestinationByRide(scheduledRide);

        assertNotNull(rsDTO);
        assertNotNull(rsDTO.getVehicleStatus());
        assertNotNull(rsDTO.getRide());
        assertEquals(RideStatus.SCHEDULED ,rsDTO.getRide().getStatus());
        assertNotNull(rsDTO.getRide().getRouteCoordinates());
        assertNotNull(rsDTO.getRide().getDestination());
        assertEquals(longLat, rsDTO.getRide().getDestination());
    }

    @ParameterizedTest()
    @MethodSource(value = "getRideStatus")
    public void testGetRideForSimulation_WhenRideForVehicleDeniedOrEnded(RideStatus status) {
        Long vehicleId = 1L;
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);
        Ride denied = new Ride();
        denied.setStatus(status);
        denied.setVehicle(vehicle);
        Destination destination = new Destination();
        destination.setLongitude(1);
        destination.setLatitude(2);
        List<Ride> rides = Arrays.asList(denied);

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);
        when(destinationService.getStartDestinationByRide(denied)).thenReturn(destination);

        denied.setRouteLocation("encodedRouteLocation");

        RideSimulationDTO rsDTO = rideService.getRideForSimulation(vehicleId);

        verify(vehicleService).getVehicleById(vehicleId);
        verify(vehicleService).save(vehicle);
        verify(rideRepository).findRideByVehicleAndStatus(vehicle);
        verify(destinationService, never()).getStartDestinationByRide(denied);

        assertNotNull(rsDTO);
        assertNotNull(rsDTO.getVehicleStatus());
        assertNull(rsDTO.getRide());
    }

    static List<RideStatus> getRideStatus() {
        return Arrays.asList(RideStatus.DENIED, RideStatus.ENDED);
    }

    @Test
    public void testUpdateVehicleLocation_Success() {
        Long id = 1L;
        Double longitude = 1.1;
        Double latitude = 2.2;
        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);

        List<Ride> rides = Arrays.asList(ride);

        when(vehicleService.getVehicleById(id)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);

        ActiveVehicleDTO result = rideService.updateVehicleLocation(id, longitude, latitude);

        verify(vehicleService, times(1)).getVehicleById(id);
        verify(rideRepository, times(1)).findRideByVehicleAndStatus(vehicle);
        verify(vehicleService, times(1)).save(vehicle);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
    }

    @Test
    public void testUpdateVehicleLocation_VehicleNotPresent() {
        Long id = 1L;
        Double longitude = 1.1;
        Double latitude = 2.2;
        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        Optional<Vehicle> vehicleOpt = Optional.empty();

        List<Ride> rides = Arrays.asList(ride);

        when(vehicleService.getVehicleById(id)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);

        ActiveVehicleDTO result = rideService.updateVehicleLocation(id, longitude, latitude);

        verify(vehicleService, times(1)).getVehicleById(id);
        verify(rideRepository, never()).findRideByVehicleAndStatus(vehicle);
        verify(vehicleService, never()).save(vehicle);

        assertNull(result);
    }

    @Test
    public void testUpdateVehicleLocation_NoRideForVehicle() {
        Long id = 1L;
        Double longitude = 1.1;
        Double latitude = 2.2;
        Vehicle newVehicle = new Vehicle();
        newVehicle.setId(2L);
        newVehicle.setDriver(client);

        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        Optional<Vehicle> vehicleOpt = Optional.of(newVehicle);

        List<Ride> rides = Arrays.asList(ride);

        when(vehicleService.getVehicleById(id)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(newVehicle)).thenReturn(rides);

        ActiveVehicleDTO result = rideService.updateVehicleLocation(id, longitude, latitude);

        verify(vehicleService, times(1)).getVehicleById(id);
        verify(rideRepository, times(1)).findRideByVehicleAndStatus(newVehicle);
        verify(vehicleService, times(1)).save(newVehicle);

        assertNotNull(result);
        assertEquals(newVehicle.getId(), result.getId());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
        assertTrue(result.isFree());
    }

    @ParameterizedTest()
    @MethodSource(value = "getRideStatus")
    public void testUpdateVehicleLocation_DeniedOrEndedRideForVehicle(RideStatus status) {
        Long id = 1L;
        Double longitude = 1.1;
        Double latitude = 2.2;

        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);
        ride.setStatus(status);
        List<Ride> rides = Arrays.asList(ride);

        when(vehicleService.getVehicleById(id)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);

        ActiveVehicleDTO result = rideService.updateVehicleLocation(id, longitude, latitude);

        verify(vehicleService, times(1)).getVehicleById(id);
        verify(rideRepository, times(1)).findRideByVehicleAndStatus(vehicle);
        verify(vehicleService, times(1)).save(vehicle);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
        assertTrue(result.isFree());
    }

    @Test
    public void testUpdateVehicleLocation_ScheduledRideForVehicle() {
        Long id = 1L;
        Double longitude = 1.1;
        Double latitude = 2.2;

        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);
        ride.setStatus(RideStatus.SCHEDULED);
        List<Ride> rides = Arrays.asList(ride);

        when(vehicleService.getVehicleById(id)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);

        ActiveVehicleDTO result = rideService.updateVehicleLocation(id, longitude, latitude);

        verify(vehicleService, times(1)).getVehicleById(id);
        verify(rideRepository, times(1)).findRideByVehicleAndStatus(vehicle);
        verify(vehicleService, times(1)).save(vehicle);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
        assertTrue(result.isFree());
    }

    @Test
    public void testUpdateVehicleLocation_ScheduledAndConfirmedRideForVehicle() {
        Long id = 1L;
        Double longitude = 1.1;
        Double latitude = 2.2;

        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        Optional<Vehicle> vehicleOpt = Optional.of(vehicle);
        Ride scheduledRide = new Ride();
        scheduledRide.setVehicle(vehicle);
        scheduledRide.setStatus(RideStatus.SCHEDULED);
        List<Ride> rides = Arrays.asList(ride, scheduledRide);

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(vehicle.getLatitude());
        locationDTO.setLongitude(vehicle.getLongitude());

        when(vehicleService.getVehicleById(id)).thenReturn(vehicleOpt);
        when(rideRepository.findRideByVehicleAndStatus(vehicle)).thenReturn(rides);

        ActiveVehicleDTO result = rideService.updateVehicleLocation(id, longitude, latitude);

        verify(vehicleService, times(1)).getVehicleById(id);
        verify(rideRepository, times(1)).findRideByVehicleAndStatus(vehicle);
        verify(vehicleService, times(1)).save(vehicle);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
        assertFalse(result.isFree());
    }

    @Test
    public void getRide_RideNull() {
        List<Ride> goodRides = new ArrayList<>();
        Ride result = rideService.getRide(goodRides);
        assertNull(result);
    }

    @Test
    public void getRide_RideConfirmed() {
        List<Ride> goodRides = new ArrayList<>();
        goodRides.add(ride);
        Ride result = rideService.getRide(goodRides);
        assertNotNull(result);
        assertEquals(RideStatus.CONFIRMED, result.getStatus());
    }

    @ParameterizedTest()
    @MethodSource(value = "getRideStatus")
    public void getRide_RideDeniedOrEnded(RideStatus status) {
        List<Ride> goodRides = new ArrayList<>();
        ride.setStatus(status);
        goodRides.add(ride);
        Ride result = rideService.getRide(goodRides);
        assertNull(result);
    }

    @Test
    public void getRide_RidesScheduledAndStarted() {
        List<Ride> goodRides = new ArrayList<>();
        ride.setStatus(RideStatus.STARTED);
        goodRides.add(ride);
        Ride scheduledRide = new Ride();
        scheduledRide.setVehicle(vehicle);
        scheduledRide.setStatus(RideStatus.SCHEDULED);
        goodRides.add(scheduledRide);
        Ride result = rideService.getRide(goodRides);
        assertNotNull(result);
        assertEquals(RideStatus.STARTED, result.getStatus());
    }

    @Test
    public void getRide_RidesDeniedAndEnded() {
        List<Ride> goodRides = new ArrayList<>();
        ride.setStatus(RideStatus.ENDED);
        goodRides.add(ride);
        Ride deniedRide = new Ride();
        deniedRide.setVehicle(vehicle);
        deniedRide.setStatus(RideStatus.DENIED);
        goodRides.add(deniedRide);
        Ride result = rideService.getRide(goodRides);
        assertNull(result);
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