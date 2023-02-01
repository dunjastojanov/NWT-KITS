package com.uber.rocket.ride_booking.utils.ride;

import com.uber.rocket.dto.ChangeStatusDTO;
import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.dto.VehicleDTO;
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

import  com.uber.rocket.ride_booking.utils.user.UserCreationService.*;

import static com.uber.rocket.ride_booking.utils.user.UserCreationService.*;

public class RideCreationService {
    public  RideDTO getRideDTO() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setDestinations(DestinationCreation.getDestinationDTOList());
        return rideDTO;
    }

    public  ArrayList<Passenger> getPassengers() {
        ArrayList<Passenger> list = new ArrayList<>();
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        return list;
    }

    public  User getGoodUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jelena@gmail.com");
        return user;
    }

    public  UserDTO getUserDTOWithBlankEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("");
        return userDTO;
    }

    public  UserDTO getGoodUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("jelena@gmail.com");
        return userDTO;
    }


    public  Ride getRideEntityWithPassengers() {
        Ride ride = new Ride();
        ride.setPassengers(getPassengers());
        ride.setId(1L);
        return ride;
    }

    public  RideDTO getRideDTOWithNonexistentUser() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getUserDTOWithBlankEmail());
        return rideDTO;
    }

    public  RideDTO getRideDTOWithNoRidingPals() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        return rideDTO;
    }

    public  RideDTO getRideDTOWithNoVehicle() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        return rideDTO;
    }

    public  VehicleDTO getGoodVehicleDto() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setType(VehicleType.COUPE);
        vehicleDTO.setPetFriendly(false);
        vehicleDTO.setKidFriendly(false);
        return vehicleDTO;
    }

    public  RideDTO getRideDTOWithNoVehicleFeatures() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        return rideDTO;

    }

    public  RideDTO getRideDTOWithBadTimeString() {
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

    public  RideDTO getGoodRideDTO() {
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

    public  Ride getGoodRideFromRideDTO() {
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


    public  Ride getRideIsRequestedPassengersDenied() {
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

    public  Ride getRideNoPassengers() {
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


    public  Ride getRideIsRequestedPassengersAccepted() {
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

    public  Ride getRideOnePassengerHasMoney() {
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

    public  Ride getRideSplitFareFalseClientHasNoMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(false);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithNoMoney());
        return ride;
    }

    public  Ride getRideSplitFareFalseClientHasMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(false);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        Passenger passenger = getPassengerWithMoney();
        passenger.getUser().setTokens((double) 210);
        ride.getPassengers().add(getPassengerWithMoney());
        return ride;
    }

    public  Ride getRideSplitFareTrueClientsHasNoMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(true);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithMoney());
        ride.getPassengers().add(getPassengerWithNoMoney());
        return ride;
    }

    public  Ride getRideSplitFareTrueClientsHasMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(true);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithMoney());
        ride.getPassengers().add(getPassengerWithMoney());
        return ride;
    }


    public  List<Ride> getRides() {
        List<Ride> list = new ArrayList<>();
        list.add(getGoodRideFromRideDTO());
        return list;
    }

    public  ChangeStatusDTO getDeniedChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }

    public  ChangeStatusDTO getWaitingChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.WAITING);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }

    public  ChangeStatusDTO getAcceptedChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.ACCEPTED);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }

}
