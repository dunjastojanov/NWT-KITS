package com.uber.rocket.service;

import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    @Autowired
    DestinationRepository repository;

    public List<Destination> getDestinationsByRide(Ride ride) {
        return repository.findAllByRide(ride);
    }

    public String getStartAddressByRide(Ride ride) {
        List<Destination> destinations = this.getDestinationsByRide(ride);
        return destinations.get(0).getAddress();
    }

    public String getEndAddressByRide(Ride ride) {
        List<Destination> destinations = this.getDestinationsByRide(ride);
        return destinations.get(destinations.size() - 1).getAddress();
    }
}
