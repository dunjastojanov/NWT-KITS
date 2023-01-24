package com.uber.rocket.mapper;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.repository.FavouriteRouteRepository;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.RideService;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Transactional
public class RideMapper implements Mapper<Ride, RideDTO> {
    @Autowired
    UserService userService;

    @Autowired
    DestinationService destinationService;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    FavouriteRouteRepository favouriteRouteRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public RideDTO mapToDto(Ride ride) {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setRideId(ride.getId());
        List<Passenger> passengers = ride.getPassengers().stream().toList();
        rideDTO.setRidingPals(new ArrayList<>());
        rideDTO.setFeatures(new ArrayList<>());

        for (int i = 0; i < passengers.size(); i++) {
            User user = passengers.get(i).getUser();
            if (i == 0) {
                rideDTO.setClient(this.createUserDTO(user));
            } else {
                rideDTO.addRidingPal(this.createStatusUserDTO(user, passengers.get(i).getUserRidingStatus()));
            }
        }
        VehicleDTO vehicleDTO = new VehicleDTO();
        Vehicle vehicle = ride.getVehicle();
        if (vehicle != null) {
            vehicleDTO.setType(vehicle.getVehicleType());
            vehicleDTO.setLongitude(vehicle.getLongitude());
            vehicleDTO.setLatitude(vehicle.getLatitude());

            User user = vehicle.getDriver();
            UserRidingStatus status;
            if (ride.getStatus() == RideStatus.REQUESTED) {
                status = UserRidingStatus.WAITING;
            }  else if (ride.getStatus() == RideStatus.DENIED) {
                status = UserRidingStatus.DENIED;
            } else {
                status = UserRidingStatus.ACCEPTED;
            }
            rideDTO.setDriver(this.createStatusUserDTO(user, status));
        } else {
            vehicleDTO.setType(ride.getVehicleTypeRequested());
        }
        rideDTO.setVehicle(vehicleDTO);

        List<String> features = new ArrayList<>();
        if (ride.isKidFriendly()) features.add("Kid friendly");
        if (ride.isPetFriendly()) features.add("Pet friendly");
        rideDTO.setFeatures(features);

        rideDTO.setRoute(ride.getRouteLocation());

        LocalDateTime dateTime = ride.getStartTime();
        OffsetDateTime offsetDateTime = dateTime.atOffset(ZoneOffset.UTC);
        String formattedDateTime = offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        rideDTO.setTime(formattedDateTime);

        rideDTO.setRideStatus(ride.getStatus());

        rideDTO.setIsSplitFair(ride.getSplitFare());

        rideDTO.setPrice(ride.getPrice());

        rideDTO.setEstimatedTime(ride.getDuration());

        rideDTO.setEstimatedDistance(ride.getLength());

        rideDTO.setIsNow(ride.isNow());

        List<Destination> destinations = this.destinationService.getDestinationsByRide(ride);
        for (int i = 0; i< destinations.size();i++) {
            rideDTO.addDestination(this.mapToDestinationDTO(destinations.get(i), i));
        }

        FavouriteRoute favouriteRoute = favouriteRouteRepository.findByUserAndRide(passengers.get(0).getUser(), ride);
        if (favouriteRoute != null) {
            rideDTO.setIsRouteFavorite(true);
        } else {
            rideDTO.setIsRouteFavorite(false);
        }
        return rideDTO;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public DestinationDTO mapToDestinationDTO(Destination destination, int i) {
        DestinationDTO destinationDTO = new DestinationDTO();
        destinationDTO.setAddress(destination.getAddress());
        destinationDTO.setLatitude(destination.getLatitude());
        destinationDTO.setLongitude(destination.getLongitude());
        destinationDTO.setIndex(i);
        return destinationDTO;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public UserDTO createUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRoles().iterator().next().getRole());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setProfilePicture(user.getProfilePicture());
        return userDTO;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public StatusUserDTO createStatusUserDTO(User user, UserRidingStatus status) {
        StatusUserDTO userDTO = new StatusUserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRoles().iterator().next().getRole());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setStatus(status);
        return userDTO;
    }

    @Transactional(Transactional.TxType.REQUIRED)
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

        ride.setStartTime(LocalDateTime.parse(rideDTO.getTime(), formatter));

        ride.setStatus(rideDTO.getRideStatus());

        ride.setSplitFare(rideDTO.getIsSplitFair());

        ride.setPrice(rideDTO.getPrice());

        ride.setDuration(rideDTO.getEstimatedTime());

        ride.setLength(rideDTO.getEstimatedDistance());

        ride.setNow(rideDTO.getIsNow());
        return ride;
    }

    @Transactional(Transactional.TxType.REQUIRED)
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
