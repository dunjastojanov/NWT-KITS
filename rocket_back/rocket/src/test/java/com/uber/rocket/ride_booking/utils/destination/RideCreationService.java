package com.uber.rocket.ride_booking.utils.destination;

import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.User;

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
        list.add(new Passenger(getUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getUser(), UserRidingStatus.ACCEPTED));
        list.add(new Passenger(getUser(), UserRidingStatus.ACCEPTED));
        return list;
    }

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    public static UserDTO getUserDTOWithBlankEmail() {
        return new UserDTO();
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
}
