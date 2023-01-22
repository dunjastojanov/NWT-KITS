package com.uber.rocket.mapper;

import com.uber.rocket.dto.DestinationDTO;
import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.dto.StatusUserDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.RideService;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RideMapper implements Mapper<Ride, RideDTO> {
    @Autowired
    UserService userService;

    @Autowired
    DestinationService destinationService;

    @Autowired
    PassengerRepository passengerRepository;

    DateTimeFormatter entityFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public RideDTO mapToDto(Ride ride) {
        return null;
    }

    public Ride mapToEntity(RideDTO rideDTO) {
        Ride ride = new Ride();
        List<Passenger> passengers = new ArrayList<Passenger>();
        List<UserRidingStatus> passengersStatus = new ArrayList<UserRidingStatus>();
        User user = this.userService.getUserByEmail(rideDTO.getClient().getEmail());
        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        passengers.add(passenger);
        for (StatusUserDTO statusUserDTO : rideDTO.getRidingPals()) {
            user = this.userService.getUserByEmail(statusUserDTO.getEmail());
            passenger = new Passenger();
            passenger.setUser(user);
            passenger.setUserRidingStatus(statusUserDTO.getStatus());
            passengers.add(passenger);
        }
        passengerRepository.saveAll(passengers);
        ride.setPassengers(passengers);
        ride.setVehicleTypeRequested(rideDTO.getVehicle().getType());
        for (String feature : rideDTO.getFeatures()) {
            if (feature.equals("Kid friendly")) {
                ride.setKidFriendly(true);
            }
            if (feature.equals("Pet friendly")) {
                ride.setPetFriendly(true);
            }
        }

        ride.setRouteLocation(rideDTO.getRoute());

        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), entityFormatter));

        ride.setStatus(RideStatus.REQUESTED);

        ride.setSplitFare(rideDTO.getIsSplitFair());

        ride.setPrice(rideDTO.getPrice());

        ride.setDuration(rideDTO.getEstimatedTime());

        ride.setLength(rideDTO.getEstimatedDistance());

        return ride;
    }

    public List<Destination> mapToDestination(List<DestinationDTO> destinationsDTO) {
        List<Destination> dests = new ArrayList<Destination>();
        for (DestinationDTO dto : destinationsDTO) {
            Destination dest = new Destination();
            dest.setAddress(dto.getAddress());
            dest.setLatitude(dto.getLatitude());
            dest.setLongitude(dto.getLongitude());
            dests.add(dest);
        }
        return dests;
    }
}
