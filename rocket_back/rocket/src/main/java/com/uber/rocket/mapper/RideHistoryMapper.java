package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideHistoryDTO;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RideHistoryMapper implements Mapper<Ride, RideHistoryDTO> {
    @Autowired
    SimpleUserMapper simpleUserMapper;
    @Autowired
    ReviewMapper reviewMapper;
    @Autowired
    DestinationService destinationService;

    @Override
    public RideHistoryDTO mapToDto(Ride ride) {
        RideHistoryDTO dto = new RideHistoryDTO();
        dto.setDriver(ride.getDriver().getFullName());
        dto.setPrice(ride.getPrice());
        dto.setDuration(ride.getDuration());
        dto.setStart(destinationService.getStartAddressByRide(ride));
        dto.setEnd(destinationService.getEndAddressByRide(ride));
        dto.setId(ride.getId());
        return dto;
    }
}
