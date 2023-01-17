package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideHistoryDTO;
import com.uber.rocket.entity.ride.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RideHistoryMapper implements Mapper<Ride, RideHistoryDTO> {
    @Autowired
    SimpleUserMapper simpleUserMapper;
    @Autowired
    ReviewMapper reviewMapper;

    @Override
    public RideHistoryDTO mapToDto(Ride ride) {
        RideHistoryDTO dto = new RideHistoryDTO();
        dto.setDriver(ride.getDriver().getFullName());
        dto.setPrice(ride.getPrice());
        dto.setDuration(ride.getDuration());
        dto.setStart(ride.getStart().toString());
        dto.setEnd(ride.getEnd().toString());
        dto.setId(ride.getId());
        return dto;
    }
}
