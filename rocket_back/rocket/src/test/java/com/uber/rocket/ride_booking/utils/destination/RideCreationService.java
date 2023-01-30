package com.uber.rocket.ride_booking.utils.destination;

import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.dto.VehicleDTO;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.VehicleType;

import java.util.ArrayList;

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
        VehicleDTO vehicleDTO=new VehicleDTO();
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

    public static RideDTO getGoodRideDTO() {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(1L);
        rideDTO.setClient(getGoodUserDTO());
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setVehicle(getGoodVehicleDto());
        return rideDTO;

    }
}
