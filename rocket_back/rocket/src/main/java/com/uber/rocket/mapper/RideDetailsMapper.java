package com.uber.rocket.mapper;

import com.uber.rocket.dto.RideDetails;
import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RideDetailsMapper implements Mapper<Ride, RideDetails> {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;
    private final DestinationService destinationService;
    @Autowired
    public RideDetailsMapper(ReviewMapper reviewMapper, ReviewService reviewService, DestinationService destinationService) {
        this.reviewMapper = reviewMapper;
        this.reviewService = reviewService;
        this.destinationService = destinationService;
    }

    @Override
    public RideDetails mapToDto(Ride ride) {

        return RideDetails
                .builder()
                .id(ride.getId())
                .driver(ride.getDriver().getFullName())
                .driverProfileImage(ride.getDriver().getProfilePicture())
                .start(destinationService.getStartAddressByRide(ride))
                .end(destinationService.getEndAddressByRide(ride))
                .duration(String.valueOf(ride.getDuration()))
                .price(String.valueOf(ride.getPrice()))
                .reviews(reviewService.getReviewByRide(ride).stream().map(reviewMapper::mapToDto).collect(Collectors.toList()))
                .rating(reviewService.getReviewByRide(ride).stream().map(Review::getRating).reduce((double) 0, Double::sum))
                .build();
    }
}
