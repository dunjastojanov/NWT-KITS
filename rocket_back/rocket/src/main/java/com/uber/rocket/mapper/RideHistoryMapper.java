package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideHistory;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class RideHistoryMapper implements Mapper<Ride, RideHistory> {
    @Autowired
    ReviewMapper reviewMapper;
    @Autowired
    DestinationService destinationService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public RideHistory mapToDto(Ride ride) {
        RideHistory dto = new RideHistory();
        dto.setDate(ride.getStartTime().format(formatter));
        dto.setDriver(ride.getDriver().getFullName());
        dto.setPrice(ride.getPrice());
        dto.setDuration(ride.getDuration());
        dto.setStart(destinationService.getStartAddressByRide(ride));
        dto.setEnd(destinationService.getEndAddressByRide(ride));
        dto.setId(ride.getId());
        return dto;
    }
}
