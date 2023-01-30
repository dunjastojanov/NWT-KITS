package com.uber.rocket.ride_booking.utils.destination;

import com.uber.rocket.dto.DestinationDTO;
import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;

import java.util.ArrayList;
import java.util.List;

public class DestinationCreation {
    public static Destination getDestination() {
        Destination destination = new Destination();
        destination.setLatitude(45.2423455);
        destination.setLongitude(19.8437972);
        destination.setAddress("Dr Ivana Ribara 13 Novi Sad");
        destination.setRide(new Ride());
        return destination;
    }

    public static List<Destination> getDestinationList() {
        List<Destination> destinations = new ArrayList<>();
        destinations.add(getDestination());
        return destinations;
    }


    public static DestinationDTO getDestinationDTO() {
        DestinationDTO destinationDTO = new DestinationDTO();
        destinationDTO.setLatitude(45.2423455);
        destinationDTO.setLongitude(19.8437972);
        destinationDTO.setAddress("Dr Ivana Ribara 13 Novi Sad");
        destinationDTO.setIndex(0);
        return destinationDTO;
    }

    public static List<DestinationDTO> getDestinationDTOList() {
        List<DestinationDTO> destinationDTOs = new ArrayList<>();
        destinationDTOs.add(getDestinationDTO());
        return destinationDTOs;
    }
}
