package com.uber.rocket.ride_booking.utils.ride;

import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.dto.VehicleDTO;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.ride_booking.utils.destination.DestinationCreation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.uber.rocket.ride_booking.utils.user.UserCreationService.getPassengerWithMoney;
import static com.uber.rocket.ride_booking.utils.user.UserCreationService.getPassengerWithNoMoney;

public class RideCreationService {
    public static RideDTO getRideDTO() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setDestinations(DestinationCreation.getDestinationDTOList());
        return rideDTO;
    }

    public static ArrayList<Passenger> getPassengers() {
        ArrayList<Passenger> list = new ArrayList<>();
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getGoodUser(), UserRidingStatus.ACCEPTED));
        return list;
    }

    public static User getGoodUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jelena@gmail.com");
        return user;
    }

    public static UserDTO getUserDTOWithBlankEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("");
        return userDTO;
    }

    public static UserDTO getGoodUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("jelena@gmail.com");
        return userDTO;
    }


    public static Ride getRideEntityWithPassengers() {
        Ride ride = new Ride();
        ride.setPassengers(getPassengers());
        ride.setId(1L);
        return ride;
    }

    public static RideDTO getRideDTOWithNonexistentUser() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getUserDTOWithBlankEmail());
        return rideDTO;
    }

    public static RideDTO getRideDTOWithNoRidingPals() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        return rideDTO;
    }

    public static RideDTO getRideDTOWithNoVehicle() {
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
        return vehicleDTO;
    }

    public static RideDTO getRideDTOWithNoVehicleFeatures() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        return rideDTO;

    }

    public static RideDTO getRideDTOWithBadTimeString() {
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

    public static Ride getGoodRideFromRideDTO() {
        RideDTO rideDTO = getGoodRideDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Ride ride = new Ride();
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
        return ride;
    }

    public static Ride getRideSplitFareFalseClientHasNoMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(false);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithNoMoney());
        return ride;
    }

    public static Ride getRideSplitFareFalseClientHasMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(false);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        Passenger passenger = getPassengerWithMoney();
        passenger.getUser().setTokens((double) 210);
        ride.getPassengers().add(getPassengerWithMoney());
        return ride;
    }

    public static Ride getRideSplitFareTrueClientsHasNoMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(true);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithMoney());
        ride.getPassengers().add(getPassengerWithNoMoney());
        return ride;
    }
    public static Ride getRideSplitFareTrueClientsHasMoney() {
        Ride ride = new Ride();
        ride.setSplitFare(true);
        ride.setPrice(200);
        ride.setPassengers(new ArrayList<>());
        ride.getPassengers().add(getPassengerWithMoney());
        ride.getPassengers().add(getPassengerWithMoney());
        return ride;
    }


}
