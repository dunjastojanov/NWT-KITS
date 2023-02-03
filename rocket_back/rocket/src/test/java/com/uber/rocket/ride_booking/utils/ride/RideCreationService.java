package com.uber.rocket.ride_booking.utils.ride;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.ride_booking.utils.destination.DestinationCreation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.uber.rocket.ride_booking.utils.user.UserCreationService.*;

public class RideCreationService {

    public RideDTO getRideDTO() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setDestinations(DestinationCreation.getDestinationDTOList());
        return rideDTO;
    }

    public ArrayList<Passenger> getPassengers() {
        ArrayList<Passenger> list = new ArrayList<>();
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        return list;
    }

    public User getGoodUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jelena@gmail.com");
        return user;
    }

    public UserDTO getUserDTOWithBlankEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("");
        return userDTO;
    }

    public static UserDTO getGoodUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("jelena@gmail.com");
        return userDTO;
    }


    public Ride getRideEntityWithPassengers() {
        Ride ride = new Ride();
        ride.setPassengers(getPassengers());
        ride.setId(1L);
        return ride;
    }

    public RideDTO getRideDTOWithNonexistentUser() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getUserDTOWithBlankEmail());
        return rideDTO;
    }

    public RideDTO getRideDTOWithNoRidingPals() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        return rideDTO;
    }

    public RideDTO getRideDTOWithNoVehicle() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        return rideDTO;
    }

    public static VehicleDTO getGoodVehicleDto() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setType(VehicleType.COUPE);
        vehicleDTO.setPetFriendly(false);
        vehicleDTO.setKidFriendly(false);
        vehicleDTO.setLongitude(99.1);
        vehicleDTO.setLatitude(99.1);
        return vehicleDTO;
    }

    public RideDTO getRideDTOWithNoVehicleFeatures() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        return rideDTO;

    }

    public RideDTO getRideDTOWithBadTimeString() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        rideDTO.setFeatures(new ArrayList<>());
        rideDTO.setRoute("some_route");
        rideDTO.setTime("some_time");
        return rideDTO;

    }

    public static RideDTO getGoodRideDTO() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        rideDTO.setFeatures(new ArrayList<>());
        rideDTO.setRoute("some_route");
        rideDTO.setTime("2011-12-03T10:15:30+01:00");
        rideDTO.setRideStatus(RideStatus.CONFIRMED);
        rideDTO.setIsSplitFair(true);
        rideDTO.setPrice(200);
        rideDTO.setEstimatedDistance((double) 100);
        rideDTO.setEstimatedTime((double) 100);
        rideDTO.setIsNow(true);
        return rideDTO;
    }

    public Ride getGoodRideFromRideDTO() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        List<Passenger> passengers = new ArrayList<Passenger>();
        List<UserRidingStatus> passengersStatus = new ArrayList<UserRidingStatus>();
        User user = getGoodUser();
        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        ride.setRouteLocation(rideDTO.getRoute());
        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));
        ride.setStatus(rideDTO.getRideStatus());
        ride.setSplitFare(rideDTO.getIsSplitFair());
        ride.setPrice(rideDTO.getPrice());
        ride.setDuration(rideDTO.getEstimatedTime());
        ride.setLength(rideDTO.getEstimatedDistance());
        ride.setNow(rideDTO.getIsNow());
        ride.setId(1L);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }


    public Ride getRideIsRequestedPassengersDenied() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        List<Passenger> passengers = new ArrayList<Passenger>();
        List<UserRidingStatus> passengersStatus = new ArrayList<UserRidingStatus>();
        User user = getGoodUser();
        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.DENIED);
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        ride.setRouteLocation(rideDTO.getRoute());
        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));
        ride.setStatus(rideDTO.getRideStatus());
        ride.setSplitFare(rideDTO.getIsSplitFair());
        ride.setPrice(rideDTO.getPrice());
        ride.setDuration(rideDTO.getEstimatedTime());
        ride.setLength(rideDTO.getEstimatedDistance());
        ride.setNow(rideDTO.getIsNow());
        ride.setId(1L);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }

    public Ride getRideNoPassengers() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        List<Passenger> passengers = new ArrayList<Passenger>();
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        ride.setRouteLocation(rideDTO.getRoute());
        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));
        ride.setStatus(rideDTO.getRideStatus());
        ride.setSplitFare(rideDTO.getIsSplitFair());
        ride.setPrice(rideDTO.getPrice());
        ride.setDuration(rideDTO.getEstimatedTime());
        ride.setLength(rideDTO.getEstimatedDistance());
        ride.setNow(rideDTO.getIsNow());
        ride.setId(1L);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }


    public Ride getRideIsRequestedPassengersAccepted() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        List<Passenger> passengers = new ArrayList<Passenger>();
        List<UserRidingStatus> passengersStatus = new ArrayList<UserRidingStatus>();
        User user = getGoodUser();
        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(VehicleType.CONVERTIBLE);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        ride.setRouteLocation(rideDTO.getRoute());
        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));
        ride.setStatus(rideDTO.getRideStatus());
        ride.setSplitFare(rideDTO.getIsSplitFair());
        ride.setPrice(rideDTO.getPrice());
        ride.setDuration(rideDTO.getEstimatedTime());
        ride.setLength(rideDTO.getEstimatedDistance());
        ride.setNow(rideDTO.getIsNow());
        ride.setId(1L);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }

    public Ride getRideMorePassengerYesMoney() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        List<Passenger> passengers = new ArrayList<Passenger>();
        List<UserRidingStatus> passengersStatus = new ArrayList<UserRidingStatus>();
        User user1 = getGoodUser();
        user1.setTokens((double) 10000);
        Passenger passenger1 = new Passenger();
        passenger1.setUser(user1);
        passenger1.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        passengers.add(passenger1);
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(VehicleType.CONVERTIBLE);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        ride.setRouteLocation(rideDTO.getRoute());
        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));
        ride.setStatus(rideDTO.getRideStatus());
        ride.setSplitFare(rideDTO.getIsSplitFair());
        ride.setPrice(rideDTO.getPrice());
        ride.setDuration(rideDTO.getEstimatedTime());
        ride.setLength(rideDTO.getEstimatedDistance());
        ride.setNow(rideDTO.getIsNow());
        ride.setId(1L);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }

    public Ride getRideOnePassengerHasMoney() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        ride.setVehicle(vehicle);
        List<Passenger> passengers = new ArrayList<Passenger>();
        List<UserRidingStatus> passengersStatus = new ArrayList<UserRidingStatus>();
        User user = getGoodUser();
        user.setTokens((double) 10000);
        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(VehicleType.CONVERTIBLE);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        ride.setRouteLocation(rideDTO.getRoute());
        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));
        ride.setStatus(rideDTO.getRideStatus());
        ride.setSplitFare(rideDTO.getIsSplitFair());
        ride.setPrice(rideDTO.getPrice());
        ride.setDuration(rideDTO.getEstimatedTime());
        ride.setLength(rideDTO.getEstimatedDistance());
        ride.setNow(rideDTO.getIsNow());
        ride.setId(1L);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }

    public Ride getRideSplitFareFalseClientHasNoMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(false);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithNoMoney());
        return ride;
    }

    public Ride getRideSplitFareFalseClientHasMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(false);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        Passenger passenger = getPassengerWithMoney();
        passenger.getUser().setTokens((double) 210);
        ride.getPassengers().add(getPassengerWithMoney());
        return ride;
    }

    public Ride getRideSplitFareTrueClientsHasNoMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(true);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithMoney());
        ride.getPassengers().add(getPassengerWithNoMoney());
        return ride;
    }

    public Ride getRideSplitFareTrueClientsHasMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(true);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithMoney());
        ride.getPassengers().add(getPassengerWithMoney());
        return ride;
    }


    public List<Ride> getRides() {
        List<Ride> list = new ArrayList<>();
        list.add(getGoodRideFromRideDTO());
        return list;
    }

    public ChangeStatusDTO getDeniedChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }

    public ChangeStatusDTO getWaitingChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.WAITING);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }

    public ChangeStatusDTO getAcceptedChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }

    public static RideDTO createRideDTOWithNegativeId() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(-1L);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidUserDTOId() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(-1L);
        rideDTO.setClient(userDTO);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidUserDTOFirstName() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("");
        rideDTO.setClient(userDTO);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidUserDTOLastName() {
        RideDTO rideDTO = createRideDTOWithInvalidUserDTOFirstName();
        rideDTO.getClient().setFirstName("Mika");
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidUserDTOBlankEmail() {
        RideDTO rideDTO = createRideDTOWithInvalidUserDTOLastName();
        rideDTO.getClient().setEmail("");
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidUserDTOEmailFormat() {
        RideDTO rideDTO = createRideDTOWithInvalidUserDTOBlankEmail();
        rideDTO.getClient().setEmail("msadibsadk");
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidUserDTORole() {
        RideDTO rideDTO = createRideDTOWithInvalidUserDTOBlankEmail();
        rideDTO.getClient().setEmail("mika@gmail.com");
        return rideDTO;
    }

    public static RideDTO createRideDTOWithInvalidStatusUserDto() {
        RideDTO rideDTO = createRideDTOWithInvalidUserDTORole();
        rideDTO.getClient().setRole("CLIENT");
        rideDTO.setRidingPals(null);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithNegativeEstimatedDistance() {
        RideDTO rideDTO = createRideDTOWithInvalidStatusUserDto();
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(null);
        rideDTO.setEstimatedDistance((double) -1);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithNegativeEstimatedTime() {
        RideDTO rideDTO = createRideDTOWithNegativeEstimatedDistance();
        rideDTO.setEstimatedDistance((double) 10);
        rideDTO.setEstimatedTime((double) -10);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithNegativePrice() {
        RideDTO rideDTO = createRideDTOWithNegativeEstimatedDistance();
        rideDTO.setEstimatedTime((double) 10);
        rideDTO.setPrice(-10);
        return rideDTO;
    }

    public static RideDTO createRideDTOWitBlankRoute() {
        RideDTO rideDTO = createRideDTOWithNegativePrice();
        rideDTO.setPrice(10);
        return rideDTO;
    }

    public static RideDTO createRideDTOWithValidRoute() {
        RideDTO rideDTO = createRideDTOWitBlankRoute();
        rideDTO.setRoute("_hfsGeg}wBxGtX|j@i]jBvMl@dMjBdm@uATLl@");
        return rideDTO;
    }

    public static UserDTO getValidUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLastName("Slavica");
        userDTO.setFirstName("Mika");
        userDTO.setRole("CLIENT");
        userDTO.setProfilePicture("");
        userDTO.setId(1L);
        userDTO.setEmail("slavica@gmail.com");
        return userDTO;
    }

    public static StatusUserDTO getValidVehicle(){
        StatusUserDTO userDTO=new StatusUserDTO();
        userDTO.setRole("DRIVER");
        userDTO.setEmail("dragan@gmail.com");
        userDTO.setFirstName("Dragan");
        userDTO.setLastName("Dragic");
        userDTO.setStatus(UserRidingStatus.ACCEPTED);
        userDTO.setId(1L);
        userDTO.setProfilePicture("");
        return userDTO;
    }
    public static RideDTO createValidRideDto() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRoute("_hfsGeg}wBxGtX|j@i]jBvMl@dMjBdm@uATLl@");
        rideDTO.setPrice(10);
        rideDTO.setEstimatedTime((double) 10);
        rideDTO.setEstimatedDistance((double) 10);
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        rideDTO.setClient(getValidUserDTO());
        rideDTO.setIsNow(true);
        rideDTO.setTime("1256.1");
        rideDTO.setFeatures(new ArrayList<>());
        rideDTO.setIsNow(true);
        rideDTO.setIsSplitFair(true);
        rideDTO.setRideId(1L);
        rideDTO.setDriver(getValidVehicle());
        return rideDTO;
    }


}
