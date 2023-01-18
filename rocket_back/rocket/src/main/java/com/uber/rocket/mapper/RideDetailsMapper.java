package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideDetailsDto;
import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.service.ReviewService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RideDetailsMapper implements Mapper<Ride, RideDetailsDto> {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    public RideDetailsMapper(ReviewMapper reviewMapper, ReviewService reviewService) {
        this.reviewMapper = reviewMapper;
        this.reviewService = reviewService;
    }

    @Override
    public RideDetailsDto mapToDto(Ride ride) {

        return RideDetailsDto
                .builder()
                .driver(ride.getDriver().getFullName())
                .start(ride.getStart())
                .end(ride.getEnd())
                .duration(String.valueOf(ride.getDuration()))
                .price(String.valueOf(ride.getPrice()))
                .reviews(reviewService.getReviewByRide(ride).stream().map(reviewMapper::mapToDto).collect(Collectors.toList()))
                .rating(reviewService.getReviewByRide(ride).stream().map(Review::getRating).reduce((double) 0, Double::sum))
                .build();
    }
}
