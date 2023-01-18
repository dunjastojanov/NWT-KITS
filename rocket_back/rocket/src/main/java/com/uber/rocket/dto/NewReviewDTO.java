package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewReviewDTO {
    long rideId;
    int driverRating;
    int vehicleRating;
    String description;
}
