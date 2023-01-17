package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideDetailsDto;
import com.uber.rocket.entity.ride.Ride;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
@Component
public class RideDetailsMapper implements Mapper<Ride, RideDetailsDto> {
    private final ReviewMapper reviewMapper;

    public RideDetailsMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    @Override
    public RideDetailsDto mapToDto(Ride ride) {

        return RideDetailsDto
                .builder()
                .driver(ride.getDriver().getFullName())
                .start(ride.getStart().toString())
                .end(ride.getEnd().toString())
                .duration(String.valueOf(ride.getDuration()))
                .price(String.valueOf(ride.getPrice()))
                .reviews(ride.getReviews().stream().map(reviewMapper::mapToDto).collect(Collectors.toList()))
                .rating(ride.getRating())
                .build();
    }
}
