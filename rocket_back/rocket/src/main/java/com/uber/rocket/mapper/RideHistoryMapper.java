package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideHistoryDTO;
import com.uber.rocket.entity.ride.Review;
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
        dto.setDriver(simpleUserMapper.mapToDto(ride.getDriver()));
        dto.setPassengers(ride.getPassengers().stream().map((p -> simpleUserMapper.mapToDto(p))).toList());
        dto.setPrice(ride.getPrice());
        dto.setDuration(ride.getDuration());
        dto.setStart(ride.getStart().toString());
        dto.setEnd(ride.getEnd().toString());
        dto.setReviews(ride.getReviews().stream().map(r -> reviewMapper.mapToDto(r)).toList());
        dto.setRating(ride.getReviews().stream().map(Review::getRating).reduce((float) 0, Float::sum));
        return dto;
    }
}
