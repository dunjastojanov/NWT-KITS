package com.uber.rocket.service;

import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DestinationService {

    @Autowired
    DestinationRepository repository;

    @Transactional
    public List<Destination> getDestinationsByRide(Ride ride) {
        return repository.findAllByRide(ride);
    }

    @Transactional
    public String getStartAddressByRide(Ride ride) {
        List<Destination> destinations = this.getDestinationsByRide(ride);
        return destinations.get(0).getAddress();
    }
    @Transactional
    public String getEndAddressByRide(Ride ride) {
        List<Destination> destinations = this.getDestinationsByRide(ride);
        return destinations.get(destinations.size() - 1).getAddress();
    }
    @Transactional
    public Destination getStartDestinationByRide(Ride ride) {
        List<Destination> destinations = this.getDestinationsByRide(ride);
        return destinations.get(0);
    }

    @Transactional
    public Destination getEndDestinationByRide(Ride ride) {
        List<Destination> destinations = this.getDestinationsByRide(ride);
        return destinations.get(destinations.size() - 1);
    }

    @Transactional
    public void save(Destination destination) {
        this.repository.save(destination);
    }
}
