package com.uber.rocket.mapper;

import com.uber.rocket.dto.DestinationDTO;
import com.uber.rocket.dto.RideDetails;
import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.service.DestinationService;
import com.uber.rocket.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RideDetailsMapper implements Mapper<Ride, RideDetails> {
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private DestinationService destinationService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public RideDetails mapToDto(Ride ride) {
        return RideDetails
                .builder()
                .id(ride.getId())
                .date(ride.getStartTime().format(formatter))
                .driver(ride.getDriver().getFullName())
                .driverProfileImage(ride.getDriver().getProfilePicture())
                .start(destinationService.getStartAddressByRide(ride))
                .end(destinationService.getEndAddressByRide(ride))
                .duration(String.valueOf(ride.getDuration()))
                .price(String.valueOf(ride.getPrice()))
                .passengers(ride.getPassengers().stream().map(passenger -> passenger.getUser().getFullName()).toList())
                .reviews(reviewService.getReviewByRide(ride).stream().map(reviewMapper::mapToDto).collect(Collectors.toList()))
                .rating(reviewService.getReviewByRide(ride).stream().map(Review::getRating).reduce((double) 0, Double::sum))
                .build();
    }
}
